package com.meijialife.dingdang.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.UserRateListAdapter;
import com.meijialife.dingdang.bean.UserRateListData;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 用户评价列表
 * @author yejiurui
 *
 */

public class UserRateListActivity extends BaseActivity {

	private ListView layout_user_list;

	private View loadMoreView;
	private Button loadMoreButton;
	private int pageIndex = 1;// 页码
	private ArrayList<UserRateListData> myUserList;
	private ArrayList<UserRateListData> totalUserList;
	private UserRateListAdapter userListAdapter;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_list);
		super.onCreate(savedInstanceState);

		initView();

	}

	private void initView() {
		setTitleName("用户评价");
		requestBackBtn();
		
		myUserList = new ArrayList<UserRateListData>();
		totalUserList = new ArrayList<UserRateListData>();
		userListAdapter = new UserRateListAdapter(this);
		
		layout_user_list = (ListView)findViewById(R.id.layout_user_list);
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);  
	    loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
	    layout_user_list.setAdapter(userListAdapter);
	    layout_user_list.addFooterView(loadMoreView);   //设置列表底部视图
	    getUserList(pageIndex);
	    loadMoreButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	pageIndex+=pageIndex;
	                setLoadMoreStatus(true);
	                getUserList (pageIndex);
	            }
	    });
	}
	public void getUserList(int page) {
        if (!NetworkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.net_not_open), 0).show();
            return;
        }
        String staffid = SpFileUtil.getString(this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_USER_RATE_LIST, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(UserRateListActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(UserRateListActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                setLoadMoreStatus(false);
                String errorMsg = "";
                dismissDialog();
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                myUserList = gson.fromJson(data, new TypeToken<ArrayList<UserRateListData>>() {
                                }.getType());
                                showData(myUserList);
                            }else {
                                loadMoreButton.setVisibility(View.GONE);
                                Toast.makeText(UserRateListActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
                    errorMsg = "网络繁忙，请稍后再试";

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(UserRateListActivity.this, errorMsg);
                }
            }
        });

    }
	
	 protected void showData(ArrayList<UserRateListData> myUserList) {
		 
		 if(myUserList.size()<10){
             loadMoreButton.setVisibility(View.GONE);
		 }else {
             loadMoreButton.setVisibility(View.VISIBLE);
		}
		 if(pageIndex==1){
	            totalUserList.clear();
	            for (UserRateListData data : myUserList) {
	                totalUserList.add(data);
	            }
	        }
	        if(pageIndex>=2){
	            for (UserRateListData data : myUserList) {
	            	totalUserList.add(data);
	            }
	        }
	        //给适配器赋值
	        userListAdapter.setData(totalUserList);
	}

	 @Override
	protected void onDestroy() {
		super.onDestroy();
		pageIndex = 1;
		totalUserList.clear();
		myUserList.clear();
	}
	/**
     * 设置加载更多按钮状态
     * @param isLoad 是否在加载
     */
    private void setLoadMoreStatus(boolean isLoad){
        if(isLoad){
            loadMoreButton.setText("正在加载...");
            loadMoreButton.setEnabled(false);
            loadMoreButton.setClickable(false);
        }else{
            loadMoreButton.setText("加载更多");
            loadMoreButton.setEnabled(true);
            loadMoreButton.setClickable(true);
        }
    }

}
