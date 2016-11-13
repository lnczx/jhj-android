package com.meijialife.dingdang.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.google.gson.Gson;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.OrderListDetailsAdapter;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.bean.OrderListVo.ServiceAddonsBean;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.service.LocationReportAgain;
import com.meijialife.dingdang.ui.ListViewForInner;
import com.meijialife.dingdang.ui.ToggleButton;
import com.meijialife.dingdang.ui.ToggleButton.OnToggleChanged;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 订单详情
 * 
 * @author windows7
 * 
 */

public class OrderDetailActivity extends BaseActivity {

    private UserIndexData userIndexData;
    private ImageView title_btn_right;
    private OrderListVo orderBean;
    private ImageView iv_order_type;
    private static String ORDERZDG = "zhongdiangong";
    private static String ORDERZL = "zhuli";
    private String ORDERTYPE = ORDERZDG;
    private static String START = "start";
    private static String OVER = "over";
    private TextView tv_order_over;
    private TextView tv_order_tiaozheng;
    private TextView tv_order_start;
    private ImageView iv_order_over;
    private ImageView iv_order_tiaozheng;
    private ImageView iv_order_start;
    private LinearLayout layout_order_tiaozheng;
    private View layout_order_tiaozheng_line;
    private String urlString;
    private Short order_type;
    private EditText et_input_content;
    private EditText et_input_money;
    private TextView tv_order_money;
    private Short order_status;
    private LinearLayout layout_fuwu_shichang;
    private TextView tv_order_shichang;
    private TextView tv_service_time_type;
    private TextView tv_order_remarks;
    private TextView tv_order_no;
    private TextView tv_order_time;
    private TextView tv_order_xiangmu;
    private TextView tv_order_incoming;
    private TextView tv_order_status;
    private TextView tv_service_type_name;
    private TextView btn_order_start_work;
    private TextView tv_call_phone;
    private TextView tv_goto_address;
    private TextView tv_input_money;
    private TextView tv_input_content;
    private TextView tv_order_addr;
    private String order_id;
    private LinearLayout layout_goutong_des;
    private ToggleButton slipBtn;
    private boolean isSelect;
    private LinearLayout layout_server_details;
    private ListViewForInner listview_details;
    private OrderListDetailsAdapter orderListDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_order_detail);
        super.onCreate(savedInstanceState);

        order_id = getIntent().getExtras().getString("order_id");

        initView();

        getOrderListDetail(order_id);

    }

    private void initView() {
        setTitleName("订单详情");
        requestBackBtn();
        title_btn_right = (ImageView) findViewById(R.id.title_btn_right);

        // 3个图标状态
        tv_order_over = (TextView) findViewById(R.id.tv_order_over);
        tv_order_tiaozheng = (TextView) findViewById(R.id.tv_order_tiaozheng);
        tv_order_start = (TextView) findViewById(R.id.tv_order_start);
        iv_order_over = (ImageView) findViewById(R.id.iv_order_over);
        iv_order_tiaozheng = (ImageView) findViewById(R.id.iv_order_tiaozheng);
        iv_order_start = (ImageView) findViewById(R.id.iv_order_start);

        layout_order_tiaozheng = (LinearLayout) findViewById(R.id.layout_order_tiaozheng);
        layout_fuwu_shichang = (LinearLayout) findViewById(R.id.layout_fuwu_shichang);
        layout_goutong_des = (LinearLayout) findViewById(R.id.layout_goutong_des);
        layout_order_tiaozheng_line = (View) findViewById(R.id.layout_order_tiaozheng_line);

        tv_order_remarks = (TextView) findViewById(R.id.tv_order_remarks);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_order_xiangmu = (TextView) findViewById(R.id.tv_order_xiangmu);
        tv_order_money = (TextView) findViewById(R.id.tv_order_money);
        tv_order_incoming = (TextView) findViewById(R.id.tv_order_incoming);
        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_service_type_name = (TextView) findViewById(R.id.tv_service_type_name);
        btn_order_start_work = (TextView) findViewById(R.id.btn_order_start_work);
        tv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
        tv_goto_address = (TextView) findViewById(R.id.tv_goto_address);
        iv_order_type = (ImageView) findViewById(R.id.iv_order_type);
        tv_order_shichang = (TextView) findViewById(R.id.tv_order_shichang);
        tv_service_time_type = (TextView) findViewById(R.id.tv_service_time_type);
        tv_order_addr = (TextView) findViewById(R.id.tv_order_addr);
        slipBtn = (ToggleButton) findViewById(R.id.slipBtn_fatongzhi);

      /*  slipBtn.setOnToggleChanged(new OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    isSelect = true;
                } else {
                    isSelect = false;
                }
            }
        });*/

        // 调整订单

        tv_input_content = (TextView) findViewById(R.id.tv_input_content);
        tv_input_money = (TextView) findViewById(R.id.tv_input_money);
        et_input_content = (EditText) findViewById(R.id.et_input_content);
        et_input_money = (EditText) findViewById(R.id.et_input_order_money);
        
        layout_server_details = (LinearLayout) findViewById(R.id.layout_server_details);
        listview_details = (ListViewForInner) findViewById(R.id.listview_details);
        

    }

    private void ShowData(final OrderListVo orderBean) {
        if (null != orderBean) {

            et_input_money.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String money = et_input_money.getText().toString().trim();
                    String order_ratio = orderBean.getOrder_ratio();

                    if (StringUtils.isNotEmpty(money) && money.length() > 0) {
                        Double input_money = Double.valueOf(money);
                        Double ratio = Double.valueOf(order_ratio);

                        double result = input_money * ratio;

                        String res = String.valueOf(result);

                        tv_order_incoming.setText(res + "元");
                    } else {
                        tv_order_incoming.setText("0元");
                    }

                }
            });

            title_btn_right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderBean.getTel_staff()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });

            tv_call_phone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != orderBean) {
                        String mobile = orderBean.getMobile();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });

            order_status = orderBean.getOrder_status();
            order_type = orderBean.getOrder_type();

            btn_order_start_work.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Builder dialog = new AlertDialog.Builder(OrderDetailActivity.this);
                    dialog.setTitle("提示");
                    dialog.setIcon(R.drawable.ic_launcher);
                    dialog.setMessage("确认操作吗？");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (order_type == 0 || order_type == 1) {// 钟点工
                                if (order_status == 3) {
                                    change_work(START);
                                } else if (order_status == 5) {
                                    int pay_type = orderBean.getPay_type();
                                    if (pay_type == 6 && !isSelect) {
                                        UIUtils.showToast(OrderDetailActivity.this, "请线下收款后再完成服务");
                                    } else {
                                        change_work(OVER);
                                    }
                                }
                            } else if (order_type == 2) {// 助理单
                                if (order_status == 2) {// 已派工
                                    // 调整订单
                                    change_order();
                                } else if (order_status == 4) {// 已支付
                                    change_work(START);
                                } else if (order_status == 5) {
                                    int pay_type = orderBean.getPay_type();
                                    if (pay_type == 6 && !isSelect) {
                                        UIUtils.showToast(OrderDetailActivity.this, "请线下收款后再完成服务");
                                    } else {
                                        change_work(OVER);
                                    }
                                }
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.show();

                }
            });
            tv_goto_address.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String service_addrLat = orderBean.getService_addr_lat();
                    String service_addrLng = orderBean.getService_addr_lng();
                    String service_addr = orderBean.getService_addr();
                    startPoiNearbySearch(service_addrLat, service_addrLng, service_addr);

                }
            });

            if (order_type == 0 || order_type == 1) {
                if (order_status < 3 || order_status >= 7) {
                    // 不可点
                    btn_order_start_work.setEnabled(false);
                    // btn_order_start_work.setPressed(true);
                } else {
                    btn_order_start_work.setEnabled(true);
                    // btn_order_start_work.setPressed(false);
                }
            } else if (order_type == 2) {
                if (order_status == 0 || order_status == 1 || order_status == 3 || order_status == 7) {
                    // 不可点
                    btn_order_start_work.setEnabled(false);
                    // btn_order_start_work.setPressed(true);
                } else {
                    btn_order_start_work.setEnabled(true);
                    // btn_order_start_work.setPressed(false);
                }
            }
            
            if(order_status == 5 && orderBean.getPay_type()==6  ){
                slipBtn.setVisibility(View.VISIBLE);
            }else{
                slipBtn.setVisibility(View.GONE);
            }

            slipBtn.setOnToggleChanged(new OnToggleChanged() {
				@Override
				public void onToggle(boolean on) {
					if(on){
						isSelect = true;
						tv_order_status.setText("线下已支付");
					}else {
						isSelect = false;
						tv_order_status.setText(orderBean.getPay_type_name());
					}
				}
			});
            
            
            
            tv_order_remarks.setText(orderBean.getRemarks());
            tv_order_no.setText(orderBean.getOrder_no());
            tv_order_time.setText(orderBean.getService_date());
            tv_order_xiangmu.setText(orderBean.getService_content());
            tv_order_money.setText(orderBean.getOrder_money() + "元");
            tv_order_incoming.setText(orderBean.getOrder_incoming() + "元");
            tv_order_status.setText(orderBean.getPay_type_name());
            tv_service_type_name.setText(orderBean.getService_type_name());
            btn_order_start_work.setText(orderBean.getButton_word());
            tv_input_content.setText(orderBean.getRemarks_confirm());
            tv_order_addr.setText(orderBean.getService_addr());
            // 判断哪些展示
            if (order_type == 0 || order_type == 1) {// 钟点工
                ORDERTYPE = ORDERZDG;
                dissmisOrderModify();
                iv_order_type.setBackgroundResource(R.drawable.icon_zhongdiangong);
                layout_fuwu_shichang.setVisibility(View.VISIBLE);
                tv_order_shichang.setText(orderBean.getService_hour() + "小时");
                tv_service_time_type.setText("服务时间：");

            } else if (order_type == 2) {// 助理单
                ORDERTYPE = ORDERZL;
                iv_order_type.setBackgroundResource(R.drawable.icon_shenfen);
                showOrderModify();
                layout_fuwu_shichang.setVisibility(View.GONE);
                tv_service_time_type.setText("下单时间：");
            }

            // 哪些选中
            if (order_status >= 7) {
                SelectOrderOver();
            } else if (order_status >= 5) {
                SelectOrderStart();
            } else if (order_status >= 3) {
                SelectOrderModify();
            }

            if (order_type == 2 && order_status == 2) {
                tv_input_content.setVisibility(View.GONE);
                tv_order_money.setVisibility(View.GONE);
                et_input_content.setVisibility(View.VISIBLE);
                et_input_money.setVisibility(View.VISIBLE);
            } else {
                tv_input_content.setVisibility(View.VISIBLE);
                tv_order_money.setVisibility(View.VISIBLE);
                et_input_content.setVisibility(View.GONE);
                et_input_money.setVisibility(View.GONE);
            }
            
            //列表展示
            List<ServiceAddonsBean>  orderBeans = orderBean.getService_addons();
            if(null != orderBeans && orderBeans.size()>0){
                layout_server_details.setVisibility(View.VISIBLE);
                orderListDetails = new OrderListDetailsAdapter(OrderDetailActivity.this);
                orderListDetails.setData(orderBeans);
                listview_details.setAdapter(orderListDetails);
            }else{
                layout_server_details.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 获取订单详情
     */
    public void getOrderListDetail(final String id) {
        if (!NetworkUtils.isNetworkConnected(OrderDetailActivity.this)) {
            Toast.makeText(OrderDetailActivity.this, getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(OrderDetailActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", id + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_ORDER_DETAIL, param, new AjaxCallBack<Object>() {

            private ArrayList<OrderListVo> secData;

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissDialog();
                Toast.makeText(OrderDetailActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(OrderDetailActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(OrderDetailActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                orderBean = gson.fromJson(data, OrderListVo.class);
                                ShowData(orderBean);
                                
                                try {
                                    new LocationReportAgain(OrderDetailActivity.this).reportLocationHttp();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                    UIUtils.showToast(OrderDetailActivity.this, errorMsg);
                }
            }
        });

    }
    

    /**
     * 显示调整订单
     */
    public void showOrderModify() {
        layout_order_tiaozheng.setVisibility(View.VISIBLE);
        layout_order_tiaozheng_line.setVisibility(View.VISIBLE);
        layout_goutong_des.setVisibility(View.VISIBLE);

    }

    // 隐藏调整订单
    public void dissmisOrderModify() {
        layout_order_tiaozheng.setVisibility(View.GONE);
        layout_order_tiaozheng_line.setVisibility(View.GONE);
        layout_goutong_des.setVisibility(View.GONE);
    }

    // 调整订单点亮
    public void SelectOrderModify() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_gray));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_gray));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

    }

    // 开始服务订单点亮
    public void SelectOrderStart() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_gray));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start_selected);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

    }

    // 已经完成订单点亮
    public void SelectOrderOver() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_orange));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start_selected);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over_selected);

    }

    /**
     * 开始服务或者结束服务
     */
    private void change_work(String type) {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", order_id);
        AjaxParams param = new AjaxParams(map);

        if (StringUtils.isEquals(type, START)) {
            urlString = Constants.URL_GET_START_ORDER_WORK;
        } else if (StringUtils.isEquals(type, OVER)) {
            urlString = Constants.URL_GET_OVER_ORDER_WORK;
        }
        // showDialog();
        new FinalHttp().post(urlString, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToastLong(getApplicationContext(),
                // "开始服务返回："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            OrderDetailActivity.this.finish();
                            // if (StringUtils.isNotEmpty(data)) {
                            // } else {
                            // UIUtils.showToast(getApplicationContext(),
                            // "数据错误");
                            // }
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getApplicationContext().getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getApplicationContext().getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getApplicationContext().getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }
                
                
                try {
                    new LocationReportAgain(OrderDetailActivity.this).reportLocationHttp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 调整订单
     */
    private void change_order() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.net_not_open), 0).show();
            return;
        }
        // if (null != orderBean) {
        // orderid = orderBean.getOrder_id();
        // }

        String content = et_input_content.getText().toString().trim();
        String money = et_input_money.getText().toString().trim();

        if (StringUtils.isEmpty(content)) {
            UIUtils.showToast(getApplicationContext(), "内容不能为空");
            return;
        }
        if (StringUtils.isEmpty(money)) {
            UIUtils.showToast(getApplicationContext(), "金额不能为空");
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", order_id);
        map.put("service_content", content);
        map.put("order_money", money);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_CHANGE_ORDER_WORK, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToastLong(getApplicationContext(), "调整订单返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            OrderDetailActivity.this.finish();
                            // if (StringUtils.isNotEmpty(data)) {
                            // } else {
                            // UIUtils.showToast(getApplicationContext(),
                            // "数据错误");
                            // }
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getApplicationContext().getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getApplicationContext().getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getApplicationContext().getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }
            }
        });

    }

    /**
     * 启动百度地图Poi周边检索
     */
    public void startPoiNearbySearch(String latStr, String lonStr, String addname) {
        if (StringUtils.isEmpty(latStr) || StringUtils.isEmpty(lonStr) || StringUtils.isEmpty(addname)) {
            Toast.makeText(this, "地址信息错误", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lon = Double.parseDouble(lonStr);

        LatLng ptCenter = new LatLng(lat, lon);
        PoiParaOption para = new PoiParaOption().key(addname).center(ptCenter).radius(2000);

        try {
            BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

}
