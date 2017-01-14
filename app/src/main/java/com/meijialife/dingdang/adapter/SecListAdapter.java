package com.meijialife.dingdang.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.SecListData;
import com.meijialife.dingdang.ui.RoundImageView;

/**
 * 用户列表适配器
 *
 */
public class SecListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<SecListData> list;
	private Context context;
	private FinalBitmap finalBitmap;

	public SecListAdapter(Context context) {
	    this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<SecListData>();
		finalBitmap = FinalBitmap.create(context);
	}

	public void setData(ArrayList<SecListData> list) {
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
			convertView = inflater.inflate(R.layout.item_sec_list, null);
			holder.m_sec_name = (TextView) convertView.findViewById(R.id.m_sec_mobile);
            holder.m_sec_mobile = (TextView) convertView.findViewById(R.id.m_sec_mobile);
            holder.m_sec_service_time = (TextView) convertView.findViewById(R.id.m_sec_service_time);
            holder.m_sec_headimg = (RoundImageView) convertView.findViewById(R.id.m_sec_headimg);
            convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final SecListData secListData = list.get(position);
		
		holder.m_sec_name.setText(secListData.getName());
		holder.m_sec_mobile.setText(secListData.getMobile());
		holder.m_sec_service_time.setText(secListData.getOrder_num()+"次");
		finalBitmap.display(holder.m_sec_headimg,secListData.getHead_img());
		
		return convertView;
	}
	class Holder {
		TextView m_sec_name;
		TextView m_sec_mobile;
		TextView m_sec_service_time;
		RoundImageView m_sec_headimg;
	}

}
