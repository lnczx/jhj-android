package com.meijialife.dingdang.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.OrderListAdapter;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.bean.UpdateInfo;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;
import com.meijialife.dingdang.utils.UpdateInfoProvider;
import com.meijialife.dingdang.utils.Utils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

 
public class HistoryOrderActivity extends BaseActivity implements OnClickListener {
	
    private RadioGroup radiogroup;
    private ArrayList<OrderListVo> secData;
    private int order_from_main=0;

    private View loadMoreView;
    private Button loadMoreButton;
    private int pageIndex = 1;//页码
    private ArrayList<OrderListVo> orderList;
    
    private ProgressDialog m_pDialog;

    private LinearLayout layout_no_order;

    private ListView layout_order_list;
    private LinearLayout layout_order_detail;
    private OrderListAdapter listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_history);
        super.onCreate(savedInstanceState);
        getOrderList(2, 1);
        
        initView();
    }

    private void initView() {
    	setTitleName("历史订单");
    	requestBackBtn();


        layout_order_list = (ListView) findViewById(R.id.layout_order_list);
        layout_no_order = (LinearLayout) findViewById(R.id.layout_no_order);
        
        orderList = new ArrayList<OrderListVo>();
        listadapter = new OrderListAdapter(this);
        layout_order_list.setAdapter(listadapter);
        
        loadMoreView = this.getLayoutInflater().inflate(R.layout.load_more, null);  
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
       
        layout_order_list.addFooterView(loadMoreView);   //设置列表底部视图
        loadMoreButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                setLoadMoreStatus(true);
                getOrderList(order_from_main, pageIndex);
            }
        });
        

 
    
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
    /**
     * 获取订单列表
     */
    public void getOrderList(final int order_from, int page) {
        if (!NetworkUtils.isNetworkConnected(HistoryOrderActivity.this)) {
            Toast.makeText(HistoryOrderActivity.this, getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(HistoryOrderActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("order_from", order_from + "");
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_ORDER_LIST, param, new AjaxCallBack<Object>() {

           

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(HistoryOrderActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(HistoryOrderActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                setLoadMoreStatus(false);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(HistoryOrderActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if(pageIndex == 1){
                                orderList.clear();
                            }
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                secData = gson.fromJson(data, new TypeToken<ArrayList<OrderListVo>>() {
                                }.getType());
                                
                                if(null!=secData&&secData.size()>0){
                                    pageIndex += 1;
                                    layout_no_order.setVisibility(View.GONE);
                                    layout_order_list.setVisibility(View.VISIBLE);
                                    for(int i = 0; i < secData.size(); i++){
                                        orderList.add(secData.get(i));
                                    }
                                    if(orderList.size()>=10){
                                        loadMoreButton.setVisibility(View.VISIBLE);
                                    }else{
                                        loadMoreButton.setVisibility(View.GONE);
                                    }
                                    
                                }else{
                                    if(pageIndex == 1){
                                        layout_no_order.setVisibility(View.VISIBLE);
                                        layout_order_list.setVisibility(View.GONE);
                                    }else{
                                        layout_order_list.setVisibility(View.VISIBLE);
                                        layout_no_order.setVisibility(View.GONE);
                                        Toast.makeText(HistoryOrderActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                listadapter.setData(orderList,order_from);
                            } else {
                                layout_no_order.setVisibility(View.VISIBLE);
                                layout_order_list.setVisibility(View.GONE);
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
                    UIUtils.showToast(HistoryOrderActivity.this, errorMsg);
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
 
 
}
