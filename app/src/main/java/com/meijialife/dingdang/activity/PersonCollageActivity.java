package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.UpdateInfo;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 更多
 *
 */
public class PersonCollageActivity extends BaseActivity implements OnClickListener {
	
	RelativeLayout rl_help; // 使用帮助
	RelativeLayout rl_agree; // 注册协议
	RelativeLayout rl_feedback; // 意见反馈
	RelativeLayout rl_about; // 关于我们
	RelativeLayout rl_update; // 检查更新
	RelativeLayout rl_service; // 联系客服
	
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_collage);
        super.onCreate(savedInstanceState);
        
        initView();

    }

    private void initView() {
    	setTitleName("叮当大学");
    	requestBackBtn();
    	getData();
    }

    @Override
    public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
	 
		case R.id.index_4_rl_agree: // 注册协议
			intent = new Intent(this, WebViewActivity.class);
			intent.putExtra("url", Constants.URL_WEB_AGREE);
			intent.putExtra("title", "注册协议");
			break;

		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}
    
    private void getData() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        AjaxParams param = new AjaxParams(map);

//        showDialog();
        new FinalHttp().get(Constants.URL_GET_UNIVERSITY_STATUS, param, new AjaxCallBack<Object>() {
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
//                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToastLong(getApplicationContext(),"叮当大学首页状态："+ t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
//                                Gson gson = new Gson();
//                                userIndexData = gson.fromJson(data, UserIndexData.class);
//                                showData();
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
