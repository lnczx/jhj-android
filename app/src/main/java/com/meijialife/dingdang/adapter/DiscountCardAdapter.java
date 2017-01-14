package com.meijialife.dingdang.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.MyDiscountCard;

/**
 * 优惠卡券适配器
 *
 */
public class DiscountCardAdapter extends BaseAdapter {
	private LayoutInflater inflater;
//	private ArrayList<DiscountCardData> list;
	private ArrayList<MyDiscountCard> list;
	private Context context;

	public DiscountCardAdapter(Context context) {
	    this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<MyDiscountCard>();
	}

	public void setData(ArrayList<MyDiscountCard> list) {
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
//			convertView = inflater.inflate(R.layout.discount_card_list_item, null);
//			holder.item_tv_name = (TextView) convertView.findViewById(R.id.item_tv_name);
//            holder.item_tv_money = (TextView) convertView.findViewById(R.id.item_tv_money);
//            holder.item_tv_date = (TextView) convertView.findViewById(R.id.item_tv_date);
//            holder.item_tv_description = (TextView) convertView.findViewById(R.id.item_tv_description);
//            holder.item_tv_introduce = (TextView)convertView.findViewById(R.id.item_tv_introduce);
//			holder.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.item_tv_name.setText(list.get(position).getService_type_name());
		holder.item_tv_money.setText("￥"+list.get(position).getValue());
		holder.item_tv_date.setText("有效期至"+list.get(position).getTo_date());
		holder.item_tv_description.setText(list.get(position).getDescription());
		holder.item_tv_introduce.setText(list.get(position).getIntroduction());
		if(position%2 == 0){
		    holder.rl_bg.setBackgroundResource(R.drawable.youhuiquan_bg_4);
		}else{
		    holder.rl_bg.setBackgroundResource(R.drawable.youhuiquan_bg_3);
		}
		
		return convertView;
	}
	class Holder {
		TextView item_tv_name;
		TextView item_tv_money;
		TextView item_tv_date;
		TextView item_tv_description;
		TextView item_tv_introduce;
		RelativeLayout rl_bg;
	}

}
