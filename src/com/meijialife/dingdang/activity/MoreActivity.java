package com.meijialife.dingdang.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.UpdateInfo;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.UpdateInfoProvider;
import com.meijialife.dingdang.utils.Utils;

/**
 * 更多
 *
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	
	RelativeLayout rl_help; // 使用帮助
	RelativeLayout rl_agree; // 用户协议
	RelativeLayout rl_feedback; // 意见反馈
	RelativeLayout rl_about; // 关于我们
	RelativeLayout rl_update; // 检查更新
	RelativeLayout rl_service; // 联系客服
	
	private TextView tv_version_new,tv_account_logout;
	private UpdateInfo updateInfo; // APP版本更新数据
	private ProgressDialog progDlg;
	// 当前应用版本号
	private String curVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_more);
        super.onCreate(savedInstanceState);
        
        initView();
		checkVersion(false);

    }

    private void initView() {
    	setTitleName("更多");
    	requestBackBtn();

    	/*rl_help = (RelativeLayout) findViewById(R.id.index_4_rl_help);*/
		rl_agree = (RelativeLayout) findViewById(R.id.index_4_rl_agree);
		rl_feedback = (RelativeLayout) findViewById(R.id.index_4_rl_feedback);
		rl_about = (RelativeLayout) findViewById(R.id.index_4_rl_about);
		rl_update = (RelativeLayout) findViewById(R.id.index_4_rl_update);
		rl_service = (RelativeLayout) findViewById(R.id.index_4_rl_service);


		tv_account_logout = (TextView) findViewById(R.id.tv_account_logout);
		tv_version_new = (TextView) findViewById(R.id.tv_version_new);
		tv_version_new.setVisibility(View.INVISIBLE);
		TextView moreNowVersionTextView = (TextView) findViewById(R.id.more_now_version_id);
		curVersion = getCurVersion();
		moreNowVersionTextView.setText(curVersion);
		
		

        /*rl_help.setOnClickListener(this);*/
        rl_agree.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_service.setOnClickListener(this);
        tv_account_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		/*case R.id.index_4_rl_help: // 使用帮助
			intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("url", Constants.URL_USER_HELP);
			intent.putExtra("title", "使用帮助");
			break;*/
		case R.id.index_4_rl_agree: // 用户协议
			intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("url", Constants.URL_WEB_AGREE);
			intent.putExtra("title", "用户协议");
			break;
		case R.id.index_4_rl_about: // 关于我们
			intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("url", Constants.URL_ABOUT_US);
			intent.putExtra("title", "关于我们");
			break;
		case R.id.index_4_rl_update: // 检查更新
			checkVersion(true);
			break;
		case R.id.index_4_rl_service: // 联系客服
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "010-58734880"));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			break;
		case R.id.tv_account_logout: // 退出登录
		    logOut();
		    break;

		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}
    
    /**
     * 退出登陆
     */
    private void logOut() {
        SpFileUtil.removeKey(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID);

     // 重新显示登陆页面
        startActivity(new Intent(MoreActivity.this, LoginActivity.class));
        if (MainActivity.activity != null) {
            MainActivity.activity.finish();
        }
        MoreActivity.this.finish();
         
    }

	/**
	 * 检查更新
	 *
	 * @param isManual
	 *            是否人工点击
	 */
	public void checkVersion(final boolean isManual) {
		if (isManual) {
			showDialog();
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL(Constants.URL_GET_VERSION);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					// 连接超时时间
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						updateInfo = new UpdateInfo();
						updateInfo = UpdateInfoProvider.getUpdateInfo(is);
						if (updateInfo != null) {
							// 解析成功
							if (isManual) {
								mHandler.sendEmptyMessage(CODE_VERSION_MANUAL_OK);
							} else {
								mHandler.sendEmptyMessage(CODE_VERSION_AUTO_OK);
							}
						} else {
							// 解析失败
							mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
						}
					} else {
						mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
				}
			}
		}).start();

	}

	private static final int CODE_VERSION_MANUAL_OK = 100; // 用户主动检测版本成功
	private static final int CODE_VERSION_AUTO_OK = 101; // 系统自动检测版本
	private static final int CODE_VERSION_ERROR = 102; // 检测版本数据解析失败

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_VERSION_MANUAL_OK:
				if (progDlg != null) {
					dismissDialog();
				}
				compareVersion(true);
				break;
			case CODE_VERSION_AUTO_OK:
				compareVersion(false);
				break;
			case CODE_VERSION_ERROR:
				tv_version_new.setVisibility(View.INVISIBLE);
				if (progDlg != null) {
					dismissDialog();
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 对比APP版本号
	 *
	 * @param isManual
	 *            是否用户主动对比的
	 */
	private void compareVersion(boolean isManual) {
		String newVersion = updateInfo.getVersion();
		curVersion = getCurVersion();
		int visibility;
		if (!newVersion.equals(curVersion)
				&& diffVersion(newVersion, curVersion) > 0) { // 有更新
			visibility = View.VISIBLE;
			if (isManual) {
				showVersionDlg();
			}
		} else {
			visibility = View.INVISIBLE;
			if (isManual) {
				Toast.makeText(this, "已是最新版本！", 0).show();
			}
		}

		if (null != tv_version_new && visibility > 0) {
			tv_version_new.setVisibility(visibility);
		}

	}

	public int diffVersion(String s1, String s2) {
		if (s1 == null && s2 == null)
			return 0;
		else if (s1 == null)
			return -1;
		else if (s2 == null)
			return 1;
		String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2
				.split("[^a-zA-Z0-9]+");
		int i1, i2, i3;
		for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
			if (ii == arr1.length)
				return ii == arr2.length ? 0 : -1;
			else if (ii == arr2.length)
				return 1;
			try {
				i1 = Integer.parseInt(arr1[ii]);
			} catch (Exception x) {
				i1 = Integer.MAX_VALUE;
			}
			try {
				i2 = Integer.parseInt(arr2[ii]);
			} catch (Exception x) {
				i2 = Integer.MAX_VALUE;
			}

			if (i1 != i2) {
				return i1 - i2;
			}
			i3 = arr1[ii].compareTo(arr2[ii]);

			if (i3 != 0)
				return i3;
		}
		return 0;
	}

	/**
	 * 更新提示
	 */
	private void showVersionDlg() {
		String msg = "版本：" + updateInfo.getVersion() + "\n" + updateInfo.getDescription();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发现新版本");
		builder.setMessage(msg);
		builder.setPositiveButton("立即升级",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
//						Intent intent = new Intent(MoreActivity.this, DownloadManagerActivity.class);
//						MoreActivity.this.startActivity(intent);
					}
				});
		builder.setNegativeButton("以后再说",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		AlertDialog dlg = builder.create();
		dlg.show();
	}

	/**
	 * 获取当前版本号
	 *
	 */
	private String getCurVersion() {
		if (null == curVersion) {
			curVersion = String.valueOf(Utils.getCurVerName(this));
		}

		return curVersion;
	}
}
