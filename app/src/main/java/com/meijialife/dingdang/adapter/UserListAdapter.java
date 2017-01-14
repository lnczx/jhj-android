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
import android.widget.TextView;

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.SecListActivity;
import com.meijialife.dingdang.bean.UserListData;

/**
 * 用户列表适配器
 *
 */
public class UserListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<UserListData> list;
	private Context context;

	public UserListAdapter(Context context) {
	    this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<UserListData>();
	}

	public void setData(ArrayList<UserListData> list) {
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
			convertView = inflater.inflate(R.layout.item_user_list, null);
			holder.m_user_mobile = (TextView) convertView.findViewById(R.id.m_user_mobile);
            holder.m_user_addr = (TextView) convertView.findViewById(R.id.m_user_mobile);
            holder.m_user_service_times = (TextView) convertView.findViewById(R.id.m_user_service_time);
            holder.m_sec_btn = (Button)convertView.findViewById(R.id.m_sec_btn);
            convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final UserListData userListData = list.get(position);
		
		holder.m_user_mobile.setText(userListData.getMobile());
		holder.m_user_addr.setText(userListData.getService_addr());
		holder.m_user_service_times.setText(userListData.getService_times()+"次");
		holder.m_sec_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,SecListActivity.class);
				intent.putExtra("user_id",userListData.getUser_id());
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	class Holder {
		TextView m_user_mobile;
		TextView m_user_addr;
		TextView m_user_service_times;
		Button m_sec_btn;
	}

}
