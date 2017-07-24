package com.meijialife.dingdang.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.LeaveListAdapter;
import com.meijialife.dingdang.bean.LeaveEntity;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 申请请假
 */

public class ApplyLeaveActivity extends BaseActivity implements OnClickListener {

    private RadioGroup radiogroup;
    private ArrayList<LeaveEntity> secData;
    private int order_from_main = 2;

    private View loadMoreView;
    private Button loadMoreButton;
    private int pageIndex = 1;//页码
    private ArrayList<LeaveEntity> orderList;

    private ProgressDialog m_pDialog;

    private LinearLayout layout_no_order;

    private ListView layout_order_list;
    private LinearLayout layout_order_detail;
    private LeaveListAdapter listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_history);
        super.onCreate(savedInstanceState);

        initView();
        getLeaveList(pageIndex);

        EventBus.getDefault().register(this);
    }

    private void initView() {
        setTitleName("请假申请");
        requestBackBtn();
        setRightText("申请请假");

        getRightView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToast(ApplyLeaveActivity.this, "申请请假");
//                getApplyLeave();
            }
        });


        layout_order_list = (ListView) findViewById(R.id.layout_order_list);
        layout_no_order = (LinearLayout) findViewById(R.id.layout_no_order);

        orderList = new ArrayList<LeaveEntity>();
        listadapter = new LeaveListAdapter(this);
        layout_order_list.setAdapter(listadapter);

        loadMoreView = this.getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);

        layout_order_list.addFooterView(loadMoreView);   //设置列表底部视图
        loadMoreButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setLoadMoreStatus(true);
                getLeaveList(pageIndex);
            }
        });


    }

    /**
     * 设置加载更多按钮状态
     *
     * @param isLoad 是否在加载
     */
    private void setLoadMoreStatus(boolean isLoad) {
        if (isLoad) {
            loadMoreButton.setText("正在加载...");
            loadMoreButton.setEnabled(false);
            loadMoreButton.setClickable(false);
        } else {
            loadMoreButton.setText("加载更多");
            loadMoreButton.setEnabled(true);
            loadMoreButton.setClickable(true);
        }
    }

    /**
     * 获取订单列表
     */
    public void getLeaveList(int page) {
        if (!NetworkUtils.isNetworkConnected(ApplyLeaveActivity.this)) {
            Toast.makeText(ApplyLeaveActivity.this, getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }

        String staffid = SpFileUtil.getString(ApplyLeaveActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_LEAVE_LIST, param, new AjaxCallBack<Object>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(ApplyLeaveActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(ApplyLeaveActivity.this, "errorMsg:" + strMsg);
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
                            if (pageIndex == 1) {
                                orderList.clear();
                            }
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                secData = gson.fromJson(data, new TypeToken<ArrayList<LeaveEntity>>() {
                                }.getType());

                                if (null != secData && secData.size() > 0) {
                                    pageIndex += 1;
                                    layout_no_order.setVisibility(View.GONE);
                                    layout_order_list.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < secData.size(); i++) {
                                        orderList.add(secData.get(i));
                                    }
                                    if (orderList.size() >= 10) {
                                        loadMoreButton.setVisibility(View.VISIBLE);
                                    } else {
                                        loadMoreButton.setVisibility(View.GONE);
                                    }

                                } else {
                                    if (pageIndex == 1) {
                                        layout_no_order.setVisibility(View.VISIBLE);
                                        layout_order_list.setVisibility(View.GONE);
                                    } else {
                                        layout_order_list.setVisibility(View.VISIBLE);
                                        layout_no_order.setVisibility(View.GONE);
                                        Toast.makeText(ApplyLeaveActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                listadapter.setData(orderList);
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
                    UIUtils.showToast(ApplyLeaveActivity.this, errorMsg);
                }
            }
        });

    }

    /**
     * 申请请假接口
     */
    public void getApplyLeave() {
        String staffid = SpFileUtil.getString(ApplyLeaveActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("id", "0");
        map.put("leaveDate", staffid);
        map.put("leaveDateEnd", staffid);
        map.put("halfDay", staffid);
        map.put("eaveStatus", "1");
//        map.put("remarks", remarks);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_DO_LEAVE, param, new AjaxCallBack<Object>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(ApplyLeaveActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(ApplyLeaveActivity.this, "errorMsg:" + strMsg);
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
                            UIUtils.showToast(ApplyLeaveActivity.this, "申请成功");
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
                    UIUtils.showToast(ApplyLeaveActivity.this, errorMsg);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


    /**
     * 刷新
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(LeaveEntity leaveEntity) {
        getLeaveList(pageIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
