package com.meijialife.dingdang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Context;
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
import com.meijialife.dingdang.bean.MsgBean;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 消息适配器
 * 
 */
public class HomeMsgListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<MsgBean> list;
    private Context context;
    private int msg_id;

    public HomeMsgListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<MsgBean>();
    }

    public void setData(ArrayList<MsgBean> secData) {
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
            convertView = inflater.inflate(R.layout.item_home_msg, null);
            holder.tv_msg_title = (TextView) convertView.findViewById(R.id.tv_msg_title);
            holder.tv_msg_time = (TextView) convertView.findViewById(R.id.tv_msg_time);
            holder.tv_msg_summary = (TextView) convertView.findViewById(R.id.tv_msg_summary);
            holder.tv_msg_goto_url = (TextView) convertView.findViewById(R.id.tv_msg_goto_url);
            holder.item_msg_close = (ImageView) convertView.findViewById(R.id.item_msg_close);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_msg_title.setText(list.get(position).getTitle());
        holder.tv_msg_time.setText(list.get(position).getAdd_time_str());
        holder.tv_msg_summary.setText(list.get(position).getSummary());
        holder.tv_msg_goto_url.setText(list.get(position).getGoto_url());

        msg_id = list.get(position).getMsg_id();
        holder.item_msg_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getMsgRead(msg_id);
            }
        });

        return convertView;
    }

    class Holder {
        TextView tv_msg_goto_url;
        TextView tv_msg_title;
        TextView tv_msg_time;
        TextView tv_msg_summary;
        ImageView item_msg_close;

    }

    /**
     * 消息已读
     * 
     * @param msg_id
     */
    private void getMsgRead(int msg_id) {
        if (!NetworkUtils.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(context, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "0");
        map.put("msg_id", msg_id + "");
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().post(Constants.URL_POST_TOTAL_TODAY, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(context, " errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
//                UIUtils.showTestToast(context, "消息已读：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                                getMsgList();
                                UIUtils.showToast(context, "消息已读" );
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
     * 获取消息
     */
    private void getMsgList() {
        if (!NetworkUtils.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(context, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "0");
        map.put("page", "1");
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().get(Constants.URL_GET_MSG_LIST, param, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(context, "消息列表errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(context, "消息列表返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {

                                Gson gson = new Gson();
                                ArrayList  secDatas = gson.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
                                }.getType());
                                setData(secDatas);
                                notifyDataSetChanged();

                            } else {
                                UIUtils.showToast(context, "数据错误");
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
