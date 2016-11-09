//package com.meijialife.dingdang.service;
//
//import android.util.Log;
//
//import com.baidu.mapapi.MKAddrInfo;
//import com.baidu.mapapi.MKBusLineResult;
//import com.baidu.mapapi.MKDrivingRouteResult;
//import com.baidu.mapapi.MKGeocoderAddressComponent;
//import com.baidu.mapapi.MKPoiResult;
//import com.baidu.mapapi.MKSearchListener;
//import com.baidu.mapapi.MKSuggestionResult;
//import com.baidu.mapapi.MKTransitRouteResult;
//import com.baidu.mapapi.MKWalkingRouteResult;
//
//public class GeoCoderParser implements MKSearchListener {
//
//	private static final String TAG = "GeoCoderParser";
//
//	@Override
//	public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
//		if (arg1 != 0) {
//			String str = String.format("%d", arg1);
//			Log.d(TAG, str);
//			return;
//		}
//		MKGeocoderAddressComponent address = arg0.addressComponents;
//		Log.d(TAG, "Location info:" + address.province + address.city + address.district);
//	}
//
//	@Override
//	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetRGCShareUrlResult(String arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
