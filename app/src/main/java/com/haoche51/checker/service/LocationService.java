package com.haoche51.checker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haoche51.checker.Checker;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.entity.HCBDloactionEntity;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.NetInfoUtil;

/**
 * 定位并向服务器发送当前坐标
 */
public class LocationService extends Service implements HCHttpCallback {

	/**
	 * 百度定位 核心类
	 */
	private LocationClient mLocationClient;
	/**
	 * 百度定位 定位模式
	 */
	private LocationMode mLocationMode = LocationMode.Hight_Accuracy;// 默认高精度
	/**
	 * 百度定位 经纬度
	 */
	private String mLocationCoor = "bd09ll";
	/**
	 * 百度定位 定位请求的间隔时间(毫秒) 默认5分钟
	 */
	private int mLocationSpan = 5 * 60 * 1000;

	// 国测局经纬度坐标系 coor=gcj02
	// 百度墨卡托坐标系 coor=bd09
	// 百度经纬度坐标系 coor=bd09ll
	/**
	 * 百度定位 回调
	 */
	private HaocheLocationListener mLocationListener;

	@Override
	public IBinder onBind(Intent intent) {
		return new MyLocationBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initLocation();
	}

	private void initLocation() {
		mLocationClient = ((CheckerApplication) getApplication()).mLocationClient;

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(mLocationMode);// 设置定位模式
		option.setCoorType(mLocationCoor);// 返回的定位结果是百度经纬度
		option.setScanSpan(mLocationSpan);// 设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);//设置是否需要地址信息，默认为无地址
		mLocationClient.setLocOption(option);
		mLocationListener = new HaocheLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		mLocationClient.start();
	}

	private void uploadLocation(HCBDloactionEntity entity) {
		if (NetInfoUtil.isNetConnected(this)) {
			OKHttpManager.getInstance().post(HCHttpRequestParam.getBaiduUploadParams(entity), this, 0);
		}
	}

	@Override
	public void onHttpStart(String action, int requestId) {

	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		android.util.Log.d("BDLocationD", "onSuccess " + response.toString());
	}

	@Override
	public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {

	}

	@Override
	public void onHttpRetry(String action, int requestId, int retryNo) {

	}

	@Override
	public void onHttpFinish(String action, int requestId) {

	}

	@Override
	public void onDestroy() {
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(mLocationListener);
		}
		super.onDestroy();
	}

	/**
	 * 记录上一次位置
	 */
	//private HCBDloactionEntity lastEntity;

	public class MyLocationBinder extends Binder {
	}

	/**
	 * 实现实位回调监听
	 */
	public class HaocheLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			((CheckerApplication) getApplication()).setLocation(location);
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			String time = location.getTime();

			android.util.Log.d("BDLocationD", "time=" + time + ",latitude=" + latitude + ",longitude=" + longitude);

			if (latitude == 0L || longitude == 0L || TextUtils.isEmpty(time))
				return;

			Checker checker = GlobalData.userDataHelper.getChecker();
			if (checker == null || checker.getId() == 0 || TextUtils.isEmpty(checker.getName()))
				return;

//			HCBDloactionEntity entity = new HCBDloactionEntity(latitude, longitude, time);
			HCBDloactionEntity mLocation = new HCBDloactionEntity(latitude, longitude, time);
			uploadLocation(mLocation);
			/*	mark for crm request always update location
		if (lastEntity == null) {
				lastEntity = entity;
				uploadLocation(entity);
			} else {
				if (!entity.equals(lastEntity)) {
					// 和上一次位置不相同才上报位置
					uploadLocation(entity);
					lastEntity = entity;
				}
			}*/
		}
	}
}
