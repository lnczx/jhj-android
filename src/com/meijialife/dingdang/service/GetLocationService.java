//package com.meijialife.dingdang.service;
//
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.GeoPoint;
//import com.baidu.mapapi.LocationListener;
//import com.baidu.mapapi.MKSearch;
//
//import android.app.Service;
//import android.content.Intent;
//import android.location.Location;
//import android.os.IBinder;
//import android.util.Log;
//
//public class GetLocationService extends Service {
//
//	protected static final String TAG = null;
//	LocationListener mLocationListener = null;
//	BMapApiDemoApp app = null;
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		app = (BMapApiDemoApp) this.getApplication();
//		if (app.mBMapMan == null) {
//			app.mBMapMan = new BMapManager(getApplication());
//			app.mBMapMan.init(app.mStrKey,
//					new BMapApiDemoApp.MyGeneralListener());
//		}
//		app.mBMapMan.start();
//		final MKSearch mMKSearch = new MKSearch();
//		mMKSearch.init(app.mBMapMan, new GeoCoderParser());
//
//		// 注册定位事件
//		mLocationListener = new LocationListener() {
//
//			@Override
//			public void onLocationChanged(Location location) {
//				Log.d(TAG, "location change");
//				if (location != null) {
//					String strLog = String.format("经度:%f\r\n" + "纬度:%f", 
//							location.getLongitude(),
//							location.getLatitude());
//					Log.d(TAG, strLog);
//					GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
//					mMKSearch.reverseGeocode(point);
//					
//					app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
//					app.mBMapMan.stop();
//				}
//			}
//		};
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flag, int startId) {
//		app.mBMapMan.getLocationManager().requestLocationUpdates(
//				mLocationListener);
//		app.mBMapMan.start();
//		return Service.START_NOT_STICKY;
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
