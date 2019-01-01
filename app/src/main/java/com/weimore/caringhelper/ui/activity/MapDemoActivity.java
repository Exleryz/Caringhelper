package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMapDemoBinding;
import com.weimore.util.L;
import com.weimore.util.ToastUtil;

import java.util.List;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class MapDemoActivity extends AppCompatActivity {

    private ActivityMapDemoBinding mBinding;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String mCity;
    private int choosePos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_map_demo);
        initView();
    }

    public static void startActivity(Context context,SuggestionResult.SuggestionInfo suggestionInfo){
        Intent intent = new Intent(context,MapDemoActivity.class);
        intent.putExtra("suggestionInfo",suggestionInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SuggestionResult.SuggestionInfo si = intent.getParcelableExtra("suggestionInfo");
        if (si!=null){
            if(choosePos==0){
                mBinding.tvOrigin.setText(si.key);
            }else {
                mBinding.tvDestination.setText(si.key);
            }
            updateMapLocation(si.getPt());
        }
    }

    private void initView() {
        initMap();
        mBinding.tvOrigin.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(mCity)){
                choosePos = 0;
               MapSearchActivity.startActivity(this,mCity);
            }
        });
        mBinding.tvDestination.setOnClickListener(v->{
            if(!TextUtils.isEmpty(mCity)){
                choosePos = 1;
                MapSearchActivity.startActivity(this,mCity);
            }
        });
    }

    /**
     * 初始化地图配置
     */
    private void initMap() {
        mBaiduMap = mBinding.map.getMap();
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        BitmapDescriptor currentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, currentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            mCity = city;
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            int errorCode = location.getLocType(); //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            List<Poi> poiList = location.getPoiList(); //获取周边POI信息

            updateMapLocation(location);
            L.d(locationDescribe);
            L.d(new Gson().toJson(poiList));
        }
    }

    private void updateMapLocation(BDLocation location){
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
        }
    }

    private void updateMapLocation(LatLng latLng){
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latLng.latitude)
                .longitude(latLng.longitude).build();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.map.onDestroy();
    }
}
