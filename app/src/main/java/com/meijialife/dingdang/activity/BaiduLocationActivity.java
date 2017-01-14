package com.meijialife.dingdang.activity;


import android.content.Context;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.meijialife.dingdang.BaseActivity;

/**
 * 百度定位Activity基类
 * 
 */
public class BaiduLocationActivity extends BaseActivity {
	
	private LocationClient mLocClient;// 定位相关
	public MyLocationListenner myListener = new MyLocationListenner(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		initLocation();
	}
	
	/**
	 * 初始化百度定位
	 */
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
		mLocClient.start();
	}
	
	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		super.onDestroy();
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		
		private Context context;
		public MyLocationListenner(Context context){
			this.context = context;
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if (location == null)
				return;
			
			MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();
			
			if(location.hasAddr()){//如果有地址信息
				String addStr = location.getAddrStr();//详细地址信息
				String province = location.getProvince();//省份
				String city = location.getCity();//城市
				String street = location.getStreet();//街道
				String lat = location.getLatitude()+"";
				String lng = location.getLongitude()+"";
				
				listener.onLocation(addStr, lat, lng, city);
				
//				String add = ""+addStr + "\n" + province + "\n" + city + "\n" + street + "\n" + lat + "\n" + lng;
//				LogOut.i("address", add);
				
				// 如果已经得到位置，销毁定位
				mLocClient.stop();
				
//				SpFileUtil.saveString(BaiduLocationActivity.this, SpFileUtil.FILE_NAME_BASE, SpFileUtil.KEY_CITY, city);
			}
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
			
		}
	}
	
	private OnLocationListener listener;
	
	public void setLocationListener(OnLocationListener listener){
		this.listener = listener;
	}
	
	public abstract static interface OnLocationListener{
		
		/**
		 * 定位成功
		 * 
		 * @param address详细地址
		 * @param lat 纬度
		 * @param lng 经度
		 * @param city 城市
		 */
		public abstract void onLocation(String address, String lat, String lng, String city);
	}
	 
}
