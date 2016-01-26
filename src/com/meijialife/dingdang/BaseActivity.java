package com.meijialife.dingdang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.meijialife.dingdang.R;
import com.umeng.analytics.MobclickAgent;


/**
 * Activity基类
 * 
 */
public class BaseActivity extends Activity{
	
	private TextView title;
	private ImageView title_btn_right;
	private ImageView title_btn_left;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initTitle();
	}
	
	/**
	 * 初始化Title
	 */
	private void initTitle(){
		title = (TextView) findViewById(R.id.header_tv_name);
		title_btn_right = (ImageView) findViewById(R.id.title_btn_right);
		title_btn_left = (ImageView) findViewById(R.id.title_btn_left);
		
		title_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    finish();
			}
		});
	}
	
	/**
	 * 重写标题返回按钮监听
	 * @param l
	 */
	public void setOnBackClickListener(OnClickListener l){
		title_btn_left.setOnClickListener(l);
	}
	
	/**
	 * 重写标题右侧按钮监听
	 * @param l
	 */
	public void setOnRightClickListener(OnClickListener l){
	    title_btn_right.setOnClickListener(l);
	}

	/**
	 * 设置标题名称
	 * 
	 * @param id	资源id
	 */
	public void setTitleName(int id) {
		String titleName = getResources().getString(id);
		setTitleName(titleName);
	}

	/**
	 * 设置标题名称
	 * 
	 * @param titleName
	 */
	public void setTitleName(String titleName) {
		title.setText(titleName);
	}
	
	/**
	 * 显示返回按钮
	 * 
	 */
	public void requestBackBtn() {
		title_btn_left.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 显示消息按钮
	 */
	public void requestRightBtn() {
		title_btn_right.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置右侧按钮背景
	 * 
	 * @param resid    图片id
	 */
	public void setRightBtnBg(int resid) {
	    title_btn_right.setBackgroundResource(resid);
	}
	
	private ProgressDialog m_pDialog;
	public void showDialog() {
		if(m_pDialog == null){
			m_pDialog = new ProgressDialog(this);
			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_pDialog.setMessage("请稍等...");
			m_pDialog.setIndeterminate(false);
			m_pDialog.setCancelable(true);
		}
		m_pDialog.show();
	}

	public void dismissDialog() {
        if (m_pDialog != null && m_pDialog.isShowing()) {
            // m_pDialog.hide();
            m_pDialog.dismiss();
            m_pDialog = null;
        }
    }
	
	@Override
	protected void onStop() {
	    // TODO Auto-generated method stub
	    super.onStop();
	    dismissDialog();
	
	}
	@Override
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
    
	@Override
	protected void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
 
}
