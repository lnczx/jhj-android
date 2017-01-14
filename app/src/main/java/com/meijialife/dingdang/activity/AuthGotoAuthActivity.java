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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 身份认证
 * 
 * @author windows
 * 
 */
public class AuthGotoAuthActivity extends BaseActivity implements OnClickListener {

    private EditText et_input_account;
    private EditText et_input_money;
    private CheckBox iv_check_1;
    private CheckBox iv_check_2;
    private CheckBox iv_check_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_goto_auth);
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        setTitleName("身份验证");
        requestBackBtn();

        et_input_account = (EditText) findViewById(R.id.et_input_account);
        et_input_money = (EditText) findViewById(R.id.et_input_money);
        iv_check_1 = (CheckBox) findViewById(R.id.iv_check_1);
        iv_check_2 = (CheckBox) findViewById(R.id.iv_check_2);
        iv_check_3 = (CheckBox) findViewById(R.id.iv_check_3);
        findViewById(R.id.btn_tixian).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_tixian:
            String name = et_input_account.getText().toString().trim();
            String idNumber = et_input_money.getText().toString().trim();

            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(idNumber)) {
                UIUtils.showToast(getApplicationContext(), "帐户名或者金额不能为空");

            } else {
                getData(name, idNumber);
            }

            break;
        default:
            break;
        }

    }

    private void getData(String name, String card_id) {
        if (!NetworkUtils.isNetworkConnected(AuthGotoAuthActivity.this)) {
            Toast.makeText(AuthGotoAuthActivity.this, getString(R.string.net_not_open), 0).show();
            return;
        }
//        if (iv_check_1.isChecked()) {
//
//        } else if (iv_check_2.isChecked()) {
//
//        } else if (iv_check_3.isChecked()) {
//
//        }

        String staffid = SpFileUtil.getString(AuthGotoAuthActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("name", name);
        map.put("card_id", card_id);
        map.put("is_hour", iv_check_1.isChecked() ? "1" : "0");
        map.put("is_am", iv_check_2.isChecked() ? "1" : "0");
        map.put("is_run", iv_check_3.isChecked() ? "1" : "0");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_AUTH, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(AuthGotoAuthActivity.this, "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(AuthGotoAuthActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToastLong(AuthGotoAuthActivity.this, "提现申请：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            UIUtils.showToast(AuthGotoAuthActivity.this, "提交成功");
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            SpFileUtil.saveInt(AuthGotoAuthActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, 1);
                            getUserData();
                            AuthGotoAuthActivity.this.finish();
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
                    UIUtils.showToast(AuthGotoAuthActivity.this, errorMsg);
                }
            }
        });

    }
    
    /**
     * 获取个人信息数据
     */
    private void getUserData() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

       String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().get(Constants.URL_GET_USER_INFO, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(getActivity(), t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                               UserIndexData userIndexData = gson.fromJson(data, UserIndexData.class);
                               SpFileUtil.saveInt(AuthGotoAuthActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, userIndexData.getAuth_status());
                            }  
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
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }
            }
        });

    }

}
