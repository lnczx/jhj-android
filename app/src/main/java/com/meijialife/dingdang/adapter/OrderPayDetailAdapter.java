package com.meijialife.dingdang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.OrderDetailActivity;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.bean.OrderPayDetail;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 交易明细适配器
 * 
 */
public class OrderPayDetailAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<OrderPayDetail> list;
    private int order_from;
    private Context context;
    private OrderPayDetail orderListVo;
    private static String START = "start";
    private static String OVER = "over";
    private String urlString;
    private Short order_type;
    private Short order_status;
    private Long order_id;

    public OrderPayDetailAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<OrderPayDetail>();
    }

    public void setData(ArrayList<OrderPayDetail> secData) {
        this.list = secData;
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
            convertView = inflater.inflate(R.layout.item_pay_detail, null);
            holder.tv_pay_money = (TextView) convertView.findViewById(R.id.tv_pay_money);
            holder.tv_pay_remark = (TextView) convertView.findViewById(R.id.tv_pay_remark);
            holder.tv_pay_number = (TextView) convertView.findViewById(R.id.tv_pay_number);
            holder.tv_pay_date = (TextView) convertView.findViewById(R.id.tv_pay_date);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        orderListVo = list.get(position);
        if (null != orderListVo) {
            holder.tv_pay_remark.setText(orderListVo.getOrder_type_name());
            holder.tv_pay_number.setText(orderListVo.getMobile());
            holder.tv_pay_date.setText(orderListVo.getAdd_time_str());
            holder.tv_pay_money.setText(orderListVo.getOrder_pay()+"元");
            
        }
        return convertView;
    }

    class Holder {
        TextView tv_pay_money;
        TextView tv_pay_date;
        TextView tv_pay_number;
        TextView tv_pay_remark;

    }

 
}
