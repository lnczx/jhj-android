package com.meijialife.dingdang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.meijialife.dingdang.R;

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
		
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_goto_auth:
			 intent=new Intent(this, AuthGotoAuthActivity.class);
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
}
