package com.meijialife.dingdang.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.SecListActivity;
import com.meijialife.dingdang.bean.UserRateListData;

/**
 * 用户評價列表适配器
 * 
 */
public class UserRateListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<UserRateListData> list;
    private Context context;

    public UserRateListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<UserRateListData>();
    }

    public void setData(ArrayList<UserRateListData> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_user_rate_list, null);
            holder.tv_mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.tv_user_type = (TextView) convertView.findViewById(R.id.tv_user_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            
            holder.tv_time_ontime_color = (TextView) convertView.findViewById(R.id.tv_time_ontime_color);
            holder.tv_time_ontime_normal = (TextView) convertView.findViewById(R.id.tv_time_ontime_normal);
            holder.tv_time_delay_normal = (TextView) convertView.findViewById(R.id.tv_time_delay_normal);
            holder.tv_time_delay_color = (TextView) convertView.findViewById(R.id.tv_time_delay_color);

            holder.m_user_service_rate = (TextView) convertView.findViewById(R.id.m_user_service_rate);
            holder.m_user_service_skill = (RatingBar) convertView.findViewById(R.id.m_user_service_skill);
            holder.m_user_service_attitude = (RatingBar) convertView.findViewById(R.id.m_user_service_attitude);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final UserRateListData userListData = list.get(position);

        holder.tv_mobile.setText(userListData.getMobile());
        holder.tv_time.setText(userListData.getAdd_time_str());
        holder.tv_user_type.setText(userListData.getUser_type_str());
        holder.m_user_service_rate.setText(userListData.getRate_content());

        holder.m_user_service_attitude.setRating(userListData.getRate_attitude());
        holder.m_user_service_skill.setRating(userListData.getRate_skill());

        float arrival_id = userListData.getRate_arrival();
        if(arrival_id == 0){
            holder.tv_time_ontime_color.setVisibility(View.VISIBLE);
            holder.tv_time_ontime_normal.setVisibility(View.GONE);
            holder.tv_time_delay_color.setVisibility(View.GONE);
            holder.tv_time_delay_normal.setVisibility(View.VISIBLE);
        }else if(arrival_id == 1){
            holder.tv_time_ontime_color.setVisibility(View.GONE);
            holder.tv_time_ontime_normal.setVisibility(View.VISIBLE);
            holder.tv_time_delay_color.setVisibility(View.VISIBLE);
            holder.tv_time_delay_normal.setVisibility(View.GONE);
        }

        return convertView;
    }

    class Holder {
        TextView tv_mobile,tv_time_delay_color,tv_time_ontime_normal,tv_time_delay_normal,tv_time_ontime_color;
        TextView tv_user_type;
        TextView tv_time;
        TextView m_user_service_rate;
        RatingBar m_user_service_attitude;
        RatingBar m_user_service_skill;
    }

}
