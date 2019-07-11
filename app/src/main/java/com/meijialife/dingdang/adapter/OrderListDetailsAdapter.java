package com.meijialife.dingdang.adapter;

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
import com.meijialife.dingdang.bean.OrderListVo.ServiceAddonsBean;
import com.meijialife.dingdang.fra.Home2Fra;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 订单服务子项
 * 
 * @author yejiurui
 * 
 */
public class OrderListDetailsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ServiceAddonsBean> list;
    private int order_from;
    private Context context;
    private ServiceAddonsBean orderListdetails;

    public OrderListDetailsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<ServiceAddonsBean>();
    }

    public void setData(List<ServiceAddonsBean> orderBeans) {
        if (null != list) {
            list.clear();
        }
        list.addAll(orderBeans);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ServiceAddonsBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_order_details, null);

            holder.tv_details_catege = (TextView) convertView.findViewById(R.id.tv_details_catege);
//            holder.tv_details_price = (TextView) convertView.findViewById(R.id.tv_details_price);
            holder.tv_details_num = (TextView) convertView.findViewById(R.id.tv_details_num);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        orderListdetails = getItem(position);
        if (null != orderListdetails) {
            holder.tv_details_catege.setText(orderListdetails.getService_addon_name() + "");
//            holder.tv_details_price.setText(orderListdetails.getPrice() + orderListdetails.getItem_unit() + "");
            holder.tv_details_num.setText(String.valueOf(orderListdetails.getItem_num()));

        }
        return convertView;
    }

    class Holder {
        TextView tv_details_catege;
//        TextView tv_details_price;
        TextView tv_details_num;

    }
}
