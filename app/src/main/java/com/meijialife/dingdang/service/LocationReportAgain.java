package com.meijialife.dingdang.service;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;

/**
 * @description： 位置上报
 * @author： YEJIURUI
 * @date:2016年11月13日
 */
public class LocationReportAgain implements BDLocationListener {

    private LocationClient mLocClient;
    private String addStr = "";
    private String lat = "";
    private String lng = "";
    private boolean location_status = false;// 是否成功获去地理置位置
    private Context mContext;

    public LocationReportAgain(Context mContext) {
        initLocation();
        this.mContext = mContext;
    }

    /**
     * 位置上报请求
     */
    public void reportLocationHttp() {
        LogOut.debug("service request try");
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }
    }

    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);

    }

    /**
     * 发送地理位置接口
     * 
     * @param city
     * @param lng
     */
    private void sendLocation(String lat, String lng, String city) {
        LogOut.debug("service send location now");
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            return;
        }
        String staffid = SpFileUtil.getString(mContext, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "1");
        map.put("lat", lat);// weidu
        map.put("lng", lng);// jingdu
        map.put("poi_name", city);
        AjaxParams param = new AjaxParams(map);
        new FinalHttp().post(Constants.URL_POST_USER_TRAIL, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (mLocClient != null && mLocClient.isStarted()) {
                    mLocClient.stop();
                    mLocClient = null;
                }
                LogOut.debug("service send location error:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                if (mLocClient != null && mLocClient.isStarted()) {
                    mLocClient.stop();
                    mLocClient = null;
                }
                super.onSuccess(t);
                String errorMsg = "";
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            LogOut.debug("service send loac success!!!!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

        addStr = location.getAddrStr();// 详细地址信息
        lat = location.getLatitude() + "";
        lng = location.getLongitude() + "";

        LogOut.debug("service lat:" + lat + " lng:" + lng + " addStr:" + addStr);
        if (location.hasAddr()) {// 如果有地址信息
            if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(addStr)) {
                location_status = true;
                sendLocation(lat, lng, addStr);
            }
            // 如果已经得到位置，销毁定位
            mLocClient.stop();
        }

    }
}
