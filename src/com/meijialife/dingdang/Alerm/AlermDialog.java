package com.meijialife.dingdang.Alerm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;

public class AlermDialog extends Dialog {

	private Context context;
	private String title;
	private String msg;
	private String mAction;

	/**
	 * 
	 * @param context
	 * @param theme
	 *            dialog主题
	 * @param title
	 *            标题
	 * @param msg
	 *            消息
	 * @param action 
	 */
	public AlermDialog(Context context, String title, String msg, String action) {
		super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.layout_alerm_dialog);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设置系统权限，否则在服务中弹不出来

		this.context = context;
		this.title = title;
		this.msg = msg;
		this.mAction = action;

		initView();
	}

	private void initView() {
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView tv_msg = (TextView) findViewById(R.id.tv_msg);
		Button btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(okListener);

		tv_title.setText(title);
		tv_msg.setText(msg);
	}

	/**
	 * 确认按钮监听事件
	 */
	View.OnClickListener okListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("mAction", mAction);
			context.startActivity(intent);
			dismiss();
		}
	};

}
