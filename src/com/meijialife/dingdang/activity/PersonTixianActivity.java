package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 申请提现
 * 
 * @author windows
 * 
 */
public class PersonTixianActivity extends BaseActivity implements OnClickListener {

    private EditText et_input_account;
    private EditText et_input_money;
    private String toalMoney = "0";
    private TextView tv_t_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_tixian);
        super.onCreate(savedInstanceState);

        toalMoney = getIntent().getStringExtra("totalMoney");
        initView();

    }

    private void initView() {
        setTitleName("申请提现");
        requestBackBtn();

        et_input_account = (EditText) findViewById(R.id.et_input_account);
        et_input_money = (EditText) findViewById(R.id.et_input_money);
        tv_t_money = (TextView) findViewById(R.id.tv_t_money);

        findViewById(R.id.btn_tixian).setOnClickListener(this);

        tv_t_money.setText("当前余额为" + toalMoney + "元");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_tixian:
            String account = et_input_account.getText().toString().trim();
            String money = et_input_money.getText().toString().trim();

            long toMoney = 0;
            long inputMoney = 0;
            try {
                toMoney = Long.valueOf(toalMoney);
                inputMoney = Long.valueOf(money);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(money)) {
                UIUtils.showToast(getApplicationContext(), "帐户名或者金额不能为空");

            } else if (inputMoney > toMoney) {
                UIUtils.showToast(getApplicationContext(), "您申请的提现金额超出可提现余额");
            } else {
                getData(money, account);

            }

            break;
        default:
            break;
        }
    }

    private void getData(String cash_money, String account) {
        if (!NetworkUtils.isNetworkConnected(PersonTixianActivity.this)) {
            Toast.makeText(PersonTixianActivity.this, getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(PersonTixianActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("cash_money", cash_money);
        map.put("account", account);
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().get(Constants.URL_GET_TIXIAN, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(PersonTixianActivity.this, "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(PersonTixianActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToastLong(PersonTixianActivity.this, "提现申请：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            UIUtils.showToast(PersonTixianActivity.this, "申请提现成功");
                            PersonTixianActivity.this.finish();
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(PersonTixianActivity.this, errorMsg);
                }
            }
        });

    }

}
