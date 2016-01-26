package com.meijialife.dingdang.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.OrderPayDetailAdapter;
import com.meijialife.dingdang.bean.OrderPayDetail;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 分享
 * 
 * 
 */
public class ShareActivity extends BaseActivity implements OnClickListener {
    private TextView btn_share; // 分享
    private EditText et_mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.share_activity);
        super.onCreate(savedInstanceState);


        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("分享", "onResume");
    }

    private void init() {
        setTitleName("分 享");
        requestBackBtn();

        btn_share = (TextView) findViewById(R.id.share_btn_share);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        btn_share.setOnClickListener(this);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.share_btn_share: // 分享
            String number = et_mobile_number.getText().toString().trim();
            if(StringUtils.isEmpty(number)){
                UIUtils.showToast(getApplicationContext(), "手机号码不能为空");
            }else{
                getData(number);
            }
            break;

        default:
            break;
        }
    }
    
    private void getData(String  number) {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("mobile", number);
        AjaxParams param = new AjaxParams(map);

         showDialog();
        new FinalHttp().post(Constants.URL_GET_TO_SHARE, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                 dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToastLong(getApplicationContext(), "交易明细返回："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) { 
                                UIUtils.showToast(getApplicationContext(), "邀请成功");
                                ShareActivity.this.finish();
                            } else {
                                UIUtils.showToast(getApplicationContext(), "数据错误");
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
