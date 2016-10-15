package com.meijialife.dingdang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.OrderDetailActivity;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.fra.Home2Fra;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 订单适配器
 * 
 */
public class OrderListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<OrderListVo> list;
    private int order_from;
    private Context context;
    private OrderListVo orderListVo;
    private static String START = "start";
    private static String OVER = "over";
    private String urlString;
    private Short order_type;
    private Short order_status;
    private long order_id=0;

    public OrderListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<OrderListVo>();
    }

    public void setData(ArrayList<OrderListVo> secData, int order_from) {
        this.list = secData;
        this.order_from = order_from;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_order_list, null);
            holder.tv_order_type = (TextView) convertView.findViewById(R.id.tv_order_type);
            holder.tv_order_incoming = (TextView) convertView.findViewById(R.id.tv_order_incoming);
            holder.tv_service_addr = (TextView) convertView.findViewById(R.id.tv_service_addr);
            holder.tv_service_addr_distance = (TextView) convertView.findViewById(R.id.tv_service_addr_distance);
            holder.tv_service_date = (TextView) convertView.findViewById(R.id.tv_service_date);
            holder.tv_service_hours = (TextView) convertView.findViewById(R.id.tv_service_hours);
            holder.tv_service_content = (TextView) convertView.findViewById(R.id.tv_service_content);
            holder.tv_order_money = (TextView) convertView.findViewById(R.id.tv_order_money);
            holder.iv_start_server = (TextView) convertView.findViewById(R.id.iv_start_server);
            holder.tv_fuwu_time = (TextView) convertView.findViewById(R.id.tv_fuwu_time);
            holder.tv_shichang = (TextView) convertView.findViewById(R.id.tv_shichang);
            holder.iv_order_type = (ImageView) convertView.findViewById(R.id.iv_order_type);
            holder.layout_order_item = (LinearLayout) convertView.findViewById(R.id.layout_order_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        orderListVo = list.get(position);
        if (null != orderListVo) {
            Short orderType = orderListVo.getOrder_type();
            if (orderType == 0) {
                holder.iv_order_type.setBackgroundResource(R.drawable.icon_zhongdiangong);
            } else if (orderType == 2) {
                holder.iv_order_type.setBackgroundResource(R.drawable.icon_shenfen);
            }

            order_type = orderListVo.getOrder_type();
            order_status = orderListVo.getOrder_status();
            order_id = orderListVo.getOrder_id();

            // 判断开始服务或者调整订单
            holder.iv_start_server.setOnClickListener(new MyAdapterListener(orderListVo));

            // 判断点击事件
            if (order_type == 0 || order_type == 1) {
                if (order_status < 3 || order_status >= 7) {
                    // 不可点
                    holder.iv_start_server.setEnabled(false);
                    // holder.iv_start_server.setPressed(true);
                } else {
                    holder.iv_start_server.setEnabled(true);
                    // holder.iv_start_server.setPressed(false);
                }
            } else if (order_type == 2) {
                if (order_status == 0 || order_status == 1 || order_status == 3 || order_status == 7) {
                    // 不可点
                    holder.iv_start_server.setEnabled(false);
                    // holder.iv_start_server.setPressed(true);
                } else {
                    holder.iv_start_server.setEnabled(true);
                    // holder.iv_start_server.setPressed(false);
                }
            }

            holder.tv_order_type.setText(orderListVo.getOrder_type_name());
            holder.tv_order_incoming.setText(orderListVo.getOrder_incoming() + "元");
            holder.tv_service_addr.setText(orderListVo.getService_addr());
            holder.tv_service_addr_distance.setText(orderListVo.getService_addr_distance());

            holder.tv_service_content.setText(orderListVo.getService_content());
            holder.tv_order_money.setText(orderListVo.getOrder_money() + "元");
            holder.iv_start_server.setText(orderListVo.getButton_word());
            holder.tv_service_date.setText(orderListVo.getService_date());

            if (order_type == 0 || order_type == 1) {// 钟点工
                holder.tv_shichang.setVisibility(View.VISIBLE);
                holder.tv_service_hours.setVisibility(View.VISIBLE);
                holder.tv_service_hours.setText(orderListVo.getService_hour() + "小时");
                holder.tv_fuwu_time.setText("服务时间：");
            } else if (order_type == 2) {// 助理单
                holder.tv_shichang.setVisibility(View.GONE);
                holder.tv_service_hours.setVisibility(View.GONE);
                holder.tv_fuwu_time.setText("下单时间：");
            }
             if(order_id!=0){
                 holder.layout_order_item.setOnClickListener(new ItemAdapterListener(order_id + ""));
             }

        }
        return convertView;
    }

    class Holder {
        TextView tv_order_type;
        TextView tv_order_incoming;
        TextView tv_service_addr;
        TextView tv_service_addr_distance;
        TextView tv_service_date;
        TextView tv_service_hours;
        TextView tv_service_content;
        TextView tv_order_money;
        TextView iv_start_server;
        TextView tv_shichang;
        TextView tv_fuwu_time;
        ImageView iv_order_type;
        LinearLayout layout_order_item;

    }

    class ItemAdapterListener implements OnClickListener {

        private String order_id;

        public ItemAdapterListener(String id) {
            order_id = id;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order_id);
            context.startActivity(intent);

        }
    }

    class MyAdapterListener implements OnClickListener {

        private OrderListVo order;
        private int type;
        private int status;

        public MyAdapterListener(OrderListVo orderListVo) {
            order = orderListVo;
        }

        @Override
        public void onClick(View v) {
            type = order.getOrder_type();
            status = order.getOrder_status();

            if (type == 2) {// 助理单
                if (status == 2) {// 已派工
                    // 调整订单
                    // change_order();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("order_id", order.getOrder_id()+"");
                    context.startActivity(intent);
                    return;
                } 
            }
            
            Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("提示");
            dialog.setIcon(R.drawable.ic_launcher);
            dialog.setMessage("确认操作吗？");
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (type == 0) {// 钟点工
                        if (status == 3) {
                            change_work(order_id + "", START);
                        } else if (order_status == 5) {
                            change_work(order_id + "", OVER);
                        }
                    } else if (type == 2) {// 助理单
//                        if (status == 2) {// 已派工
//                            // 调整订单
//                            // change_order();
//                            Intent intent = new Intent(context, OrderDetailActivity.class);
//                            intent.putExtra("orderBean", orderListVo);
//                            context.startActivity(intent);
//                        } else
                        if (status == 4) {// 已支付
                            change_work(order_id + "", START);
                        } else if (status == 5) {
                            change_work(order_id + "", OVER);
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
    }

    /**
     * 开始服务
     */
    private void change_work(String order_id, String type) {
        if (!NetworkUtils.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(context, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
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
                UIUtils.showTestToastLong(context, "errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToastLong(context, "开始服务返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            getOrderList(order_from, 1);
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = context.getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = context.getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = context.getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = context.getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = context.getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(context, errorMsg);
                }
            }
        });

    }

    /**
     * 获取订单列表
     */
    public void getOrderList(final int order_from, int page) {
        if (!NetworkUtils.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(context, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("order_from", order_from + "");
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        new FinalHttp().get(Constants.URL_GET_ORDER_LIST, param, new AjaxCallBack<Object>() {

            private ArrayList<OrderListVo> secData;

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(context, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                secData = gson.fromJson(data, new TypeToken<ArrayList<OrderListVo>>() {
                                }.getType());
                                setData(secData, order_from);
                                notifyDataSetChanged();
                            }
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = context.getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = context.getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = context.getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = context.getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = context.getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(context, errorMsg);
                }
            }
        });

    }
}
