package com.meijialife.dingdang.broadcastReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.DateUtils;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * @description：开启服务每天7:00——22::00之间每小时保存当前位置
 * @author： kerryg
 * @date:2016年4月22日 
 */
public class PostTrailService extends Service {

	private LocationClient mLocClient;// 定位相关
	public MyLocationListenner myListener = new MyLocationListenner(getApplication());
	private String addStr = "";// 详细地址信息
	private String lat = "";
	private String lng = "";

	private boolean location_status = false;// 是否成功获去地理置位置
	private MyThread myThread;
	private boolean start_flag = false;// 是否是7点到22点

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mLocClient.start();
			// 下面写请求服务器的代码
			if (location_status && start_flag) {
				sendLocation(lat, lng, addStr);
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initLocation();
		this.myThread = new MyThread();
	}

	private class MyThread extends Thread {
		@Override
		public void run() {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MARCH) + 1;
			int day = c.get(Calendar.DATE);
			String startTime = year + "-" + month + "-" + day + " " + "7:00";
			String endTime = year + "-" + month + "-" + day + " " + "22:00";

			Long startLong = DateUtils.getDateByPattern(startTime, "yyyy-MM-dd HH:mm").getTime();
			Long endLong = DateUtils.getDateByPattern(endTime, "yyyy-MM-dd HH:mm").getTime();
			Long currentTime = new Date().getTime();
			if (currentTime >= startLong && currentTime < endLong) {
				start_flag = true;
			}
			while (true) {
				try {
					// 每隔半小时向服务器发送一次请求
					Thread.sleep(1000*60*30);
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initLocation() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
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

			addStr = location.getAddrStr();// 详细地址信息
			lat = location.getLatitude() + "";
			lng = location.getLongitude() + "";
			if (location.hasAddr()) {// 如果有地址信息
				if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(addStr)) {
					location_status = true;
				}
				// 如果已经得到位置，销毁定位
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	/**
	 * 发送地理位置接口
	 * 
	 * @param city
	 * @param lng
	 */
	private void sendLocation(String lat, String lng, String city) {

		if (!NetworkUtils.isNetworkConnected(PostTrailService.this)) {
			Toast.makeText(PostTrailService.this, getString(R.string.net_not_open), 0).show();
			return;
		}
		String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
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
				UIUtils.showTestToast(PostTrailService.this, "发送位置errorMsg:" + strMsg);
				UIUtils.showToast(PostTrailService.this, "发送位置失败");
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String errorMsg = "";
				try {
					if (StringUtils.isNotEmpty(t.toString())) {
						JSONObject obj = new JSONObject(t.toString());
						int status = obj.getInt("status");
						String msg = obj.getString("msg");
						String data = obj.getString("data");
						if (status == Constants.STATUS_SUCCESS) { // 正确
							/*
							 * UIUtils.showToast(MainActivity.this, "发送位置成功");
							 */
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
					UIUtils.showToast(PostTrailService.this, errorMsg);
				}
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		startService(intent);
		mLocClient.start();
		this.myThread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (mLocClient != null && mLocClient.isStarted()) {
			mLocClient.stop();
			mLocClient = null;
		}
		super.onDestroy();
	}
}
