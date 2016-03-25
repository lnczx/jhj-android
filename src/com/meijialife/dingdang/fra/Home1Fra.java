package com.meijialife.dingdang.fra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseFragment;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.HomeMsgListAdapter;
import com.meijialife.dingdang.bean.MsgBean;
import com.meijialife.dingdang.bean.UserInfo;
import com.meijialife.dingdang.database.DBHelper;
import com.meijialife.dingdang.utils.CalendarUtils;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 首页
 * 
 * @author RUI
 * 
 */
public class Home1Fra extends BaseFragment {

    public String today_date;

    private static View layout_mask, layout_guide;
    private final static int START_WORK = 1;
    private final static int STOP_WORK = 0;
    private static int WORK_STATE;
    private View v;
    private UserInfo userInfo;

    TextView tv_service_type_ids;// 服务大类集合

    // 获取当前位置经纬度
    private String longitude = "";// 经度
    private String latitude = "";// 纬度

    private TextView tv_total_order_incoming;

    private TextView tv_total_order_money;

//    private TextView tv_total_online;

    private TextView tv_total_order, tv_now_time;

//    private Button bt_work_state;

    private FinalBitmap finalBitmap;

    private LocationClient mLocClient;// 定位相关
    public MyLocationListenner myListener = new MyLocationListenner(getActivity());

    private ListView layout_msg_list;
    private ArrayList<MsgBean> secData;

	private TextView tv_no_msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.index_1, null);
        initLocation();
        init(v);

        return v;
    }

    private void init(View v) {

        finalBitmap = FinalBitmap.create(getActivity());
        layout_mask = v.findViewById(R.id.layout_mask);
        userInfo = DBHelper.getUserInfo(getActivity());

        tv_total_order_incoming = (TextView) v.findViewById(R.id.tv_total_order_incoming);
        tv_total_order_money = (TextView) v.findViewById(R.id.tv_total_order_money);
//        tv_total_online = (TextView) v.findViewById(R.id.tv_total_online);
        tv_total_order = (TextView) v.findViewById(R.id.tv_total_order);
        tv_now_time = (TextView) v.findViewById(R.id.tv_now_time);
        tv_no_msg = (TextView) v.findViewById(R.id.tv_no_msg);
        layout_msg_list = (ListView) v.findViewById(R.id.layout_msg_list);

//        bt_work_state = (Button) v.findViewById(R.id.bt_work_state);
//        bt_work_state.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (WORK_STATE == START_WORK) {
//                    getStartWork(STOP_WORK);
//                } else if (WORK_STATE == STOP_WORK) {
//                    getStartWork(START_WORK);
//                }
//
//            }
//        });

//        layout_msg_list.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (null != secData && secData.size() > 0) {
//                    int msg_id = secData.get(position).getMsg_id();
//                    getMsgRead(msg_id);
//                }
//            }
//        });

        String todayTimeAndWeek = CalendarUtils.getTodayTimeAndWeek();
        tv_now_time.setText(todayTimeAndWeek);

        try {
			getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			getMsgList();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void showMask() {
        layout_mask.setVisibility(View.VISIBLE);
    }

    public static void GoneMask() {
        layout_mask.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取消息
     */
    private void getMsgList() {
        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
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
                UIUtils.showTestToast(getActivity(), "消息列表errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(getActivity(), "消息列表返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                            	
                                Gson gson = new Gson();
                                secData = gson.fromJson(data, new TypeToken<ArrayList<MsgBean>>() {
                                }.getType());
                                HomeMsgListAdapter adapter = new HomeMsgListAdapter(getActivity());
                                adapter.setData(secData);
                                layout_msg_list.setAdapter(adapter);
                                
                                if(null!=secData&&secData.size()>0){
                                	tv_no_msg.setVisibility(View.GONE);
                                	layout_msg_list.setVisibility(View.VISIBLE);
                                }else{
                                	tv_no_msg.setVisibility(View.VISIBLE);
                                	tv_no_msg.setText("您暂时没有消息哦");
                                	layout_msg_list.setVisibility(View.GONE);
                                }

                            } else {
                            	tv_no_msg.setVisibility(View.VISIBLE);
                            	tv_no_msg.setText("您暂时没有消息哦");
                            	layout_msg_list.setVisibility(View.GONE);
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
                            errorMsg = "网络繁忙，请稍后再试";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = "网络繁忙，请稍后再试";

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getActivity(), errorMsg);
                }
            }

        });

    }


    /**
     * 获得首页状态
     */
    private void getData() {
        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().get(Constants.URL_GET_TOTAL_TODAY, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getActivity(), "当日统计数errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(getActivity(), "当日统计数返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                showData(data);
                            } else {
                                UIUtils.showToast(getActivity(), "数据错误");
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
                    UIUtils.showToast(getActivity(), errorMsg);
                }
            }

        });

    }

    private void showData(String data) {
        try {

            JSONObject jsonObject = new JSONObject(data);
            String total_order = jsonObject.optString("total_order");
            String total_online = jsonObject.optString("total_online");
            String total_order_money = jsonObject.optString("total_order_money");
            String total_incoming = jsonObject.optString("total_incoming");
            int is_work = jsonObject.optInt("is_work");
            WORK_STATE = is_work;

            tv_total_order_incoming.setText(total_incoming);
            tv_total_order_money.setText(total_order_money);
//            tv_total_online.setText(total_online);
            tv_total_order.setText(total_order);

//            if (WORK_STATE == START_WORK) {
//                bt_work_state.setText("收工");
//            } else if (WORK_STATE == STOP_WORK) {
//                bt_work_state.setText("开工");
//            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 开工
     */
    private void getStartWork(int iswork) {
        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        // String lat = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_LAT, "");
        // String lng = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_LNG, "");

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("is_work", iswork + "");
        map.put("lat", latitude);
        map.put("lng", longitude);
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().post(Constants.URL_GET_START_WORK, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getActivity(), "开工errorMsg:" + strMsg);
                // dismissDialog();
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToast(getActivity(), "开工返回：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            // if (StringUtils.isNotEmpty(data)) {
                            // showData(data);
                            // } else {
                            // UIUtils.showToast(getActivity(), "数据错误");
                            // }

                            getData();
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
                    UIUtils.showToast(getActivity(), errorMsg);
                }
            }

        });

    }

    /**
     * 初始化百度定位
     */
    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        if (null != mLocClient) {
            mLocClient.stop();
        }
        super.onDestroy();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        private Context context;

        public MyLocationListenner(Context context) {
            this.context = context;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null)
                return;

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

            if (location.hasAddr()) {// 如果有地址信息
                String addStr = location.getAddrStr();// 详细地址信息
                String province = location.getProvince();// 省份
                String city = location.getCity();// 城市
                String street = location.getStreet();// 街道
                latitude = location.getLatitude() + "";
                longitude = location.getLongitude() + "";

                LogOut.i("lat:" + latitude, "lng:" + longitude);

                SpFileUtil.saveString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_LAT, latitude);
                SpFileUtil.saveString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_LNG, longitude);

                // 如果已经得到位置，销毁定位
                mLocClient.stop();

            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

}
