package com.meijialife.dingdang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.ui.FristSplashLayout;
import com.meijialife.dingdang.ui.FristSplashOnViewChangeListener;

public class GuideActivity extends Activity implements FristSplashOnViewChangeListener{
   
	private FristSplashLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private LinearLayout pointLLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!getFristFlag()) {
			Intent intent = new Intent(GuideActivity.this, SplashActivity.class);
			this.startActivity(intent);
			this.finish();
			return;
		}
		
		setContentView(R.layout.guide_activity);
		initView();
	}

	private void initView() {
		mScrollLayout = (FristSplashLayout) findViewById(R.id.ScrollLayout);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		layout_start = (RelativeLayout) findViewById(R.id.layout_start);
		layout_start.setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_start:
//				mScrollLayout.setVisibility(View.GONE);
//				pointLLayout.setVisibility(View.GONE);
				
				startActivity(new Intent(GuideActivity.this, SplashActivity.class));
				GuideActivity.this.finish();
				
				updateFristFlag();
			break;
			}
		}
	};
    private RelativeLayout layout_start;

	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
	
	/**
	 * 检查是否第一次打开应用
	 * @return
	 */
	private boolean getFristFlag(){
		SharedPreferences sp = getSharedPreferences("config",Context.MODE_PRIVATE);
		int Flag = sp.getInt("frist_splash", 0);
		
		return Flag == 1? false:true;
	}
	
	/**
	 * 更新第一次打开应用的标识
	 */
	private void updateFristFlag(){
		SharedPreferences sp = getSharedPreferences("config",Context.MODE_PRIVATE);
		int Flag = sp.getInt("frist_splash", 0);
		if (Flag != 1){
			Editor edit = sp.edit();
			edit.putInt("frist_splash", 1);
			edit.commit();
		}
	}
	
	
}