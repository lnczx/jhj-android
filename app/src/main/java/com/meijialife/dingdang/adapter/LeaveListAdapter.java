package com.meijialife.dingdang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.LeaveEntity;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 请假适配器
 */
public class LeaveListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<LeaveEntity> list;
    private Context context;
    private LeaveEntity leaveEntity;

    public LeaveListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<LeaveEntity>();
    }

    public void setData(ArrayList<LeaveEntity> secData) {
        this.list = secData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LeaveEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_leave_list_item, null);
            holder.tvUserType = (TextView) convertView.findViewById(R.id.tv_user_type);
            holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tv_apply_time);
            holder.tvFuwuAddress = (TextView) convertView.findViewById(R.id.tv_fuwu_address);
            holder.tvApplyStartTime = (TextView) convertView.findViewById(R.id.tv_apply_start_time);
            holder.tvFuwuTime = (TextView) convertView.findViewById(R.id.tv_fuwu_time);
            holder.tvApplyDay = (TextView) convertView.findViewById(R.id.tv_apply_day);
            holder.tvFuwuXiangmu = (TextView) convertView.findViewById(R.id.tv_fuwu_xiangmu);
            holder.tvLeaveStatus = (TextView) convertView.findViewById(R.id.tv_leave_status);
            holder.ivLeaveChange = (TextView) convertView.findViewById(R.id.iv_leave_change);
            holder.layoutOrderItem = (LinearLayout) convertView.findViewById(R.id.layout_order_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        leaveEntity = list.get(position);
        if (null != leaveEntity) {
            holder.tvApplyTime.setText(leaveEntity.getAdd_date());
            holder.tvApplyStartTime.setText(leaveEntity.getLeave_date());
            String total_days = leaveEntity.getTotal_days();
            if (StringUtils.isEquals(total_days, "上午") || StringUtils.isEquals(total_days, "下午")) {
                holder.tvApplyDay.setText(leaveEntity.getTotal_days());
            } else {
                holder.tvApplyDay.setText(leaveEntity.getTotal_days() + "天");
            }
            String status = leaveEntity.getLeave_status();
            if (StringUtils.isEquals("1", status)) {
                holder.tvLeaveStatus.setText("请假中");
                holder.ivLeaveChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        change_leave(leaveEntity.getId() + "");
                    }
                });
            } else if (StringUtils.isEquals("2", status)) {
                holder.tvLeaveStatus.setText("请假取消");
            } else if (StringUtils.isEquals("3", status)) {
                holder.tvLeaveStatus.setText("请假完成");
            }

        }
        return convertView;
    }


    static class ViewHolder {
        TextView tvUserType;
        TextView tvApplyTime;
        TextView tvFuwuAddress;
        TextView tvApplyStartTime;
        TextView tvFuwuTime;
        TextView tvApplyDay;
        TextView tvFuwuXiangmu;
        TextView tvLeaveStatus;
        TextView ivLeaveChange;
        LinearLayout layoutOrderItem;
    }


    /**
     * 取消请假
     */
    private void change_leave(String leave_id) {
        String staffid = SpFileUtil.getString(context, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("id", leave_id);
        map.put("leaveStatus", "2");
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().post(Constants.URL_GET_CANCEL_LEAVE, param, new AjaxCallBack<Object>() {
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
                            UIUtils.showToast(context, "取消成功");
                            EventBus.getDefault().post(new LeaveEntity());
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
