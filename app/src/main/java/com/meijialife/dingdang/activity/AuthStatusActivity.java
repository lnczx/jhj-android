package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

 /**
  * 申请提现
  * @author windows
  *
  */
public class AuthStatusActivity extends  Activity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_auth_status);

		initView();

	}

	private void initView() {
//		setTitleName("申请提现");
//		requestBackBtn();

		findViewById(R.id.btn_goto_auth).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_goto_auth:
		     getStaffAuth();
			break;
		case R.id.btn_logout:
			SpFileUtil.removeKey(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID);
	     // 重新显示登陆页面
	        startActivity(new Intent(AuthStatusActivity.this, LoginActivity.class));
	        if (MainActivity.activity != null) {
	            MainActivity.activity.finish();
	        }
	        AuthStatusActivity.this.finish();
	        break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	/**
	 * 获得用户身份验证状态接口
	 */
	 private void getStaffAuth() {
	        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
	            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
	            return;
	        }

	        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
	        if (StringUtils.isEmpty(staffid)) {
	            return;
	        }

	        Map<String, String> map = new HashMap<String, String>();
	        map.put("staff_id", staffid);
	        AjaxParams param = new AjaxParams(map);
	        new FinalHttp().get(Constants.URL_GET_STAFF_AUTH, param, new AjaxCallBack<Object>() {
	            @Override
	            public void onFailure(Throwable t, int errorNo, String strMsg) {
	                super.onFailure(t, errorNo, strMsg);
	                UIUtils.showTestToast(getApplicationContext(), "errorMsg:" + strMsg);
	                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
	            }

	            @Override
	            public void onSuccess(Object t) {
	                super.onSuccess(t);
	                String errorMsg = "";
	                // UIUtils.showTestToast(getActivity(), t.toString());
	                try {
	                    if (StringUtils.isNotEmpty(t.toString())) {
	                        JSONObject obj = new JSONObject(t.toString());
	                        int status = obj.getInt("status");
	                        String msg = obj.getString("msg");
	                        String data = obj.getString("data");
	                        if (status == Constants.STATUS_SUCCESS) { // 正确
	                            if (StringUtils.isNotEmpty(data)) {
	                            	if(StringUtils.isEquals(data, "1")){//data不为空，并且data=1，跳转到订单列表
	                            		startActivity(new Intent(AuthStatusActivity.this, MainActivity.class));
	                            	}
	                            }else {//data为空跳转到认证页面
	                            	startActivity(new Intent(AuthStatusActivity.this, AuthGotoAuthActivity.class));
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
