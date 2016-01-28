package com.meijialife.dingdang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.SpFileUtil;

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
			 intent=new Intent(this, AuthGotoAuthActivity.class);
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
}
