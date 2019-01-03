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
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMapDemoBinding;
import com.weimore.caringhelper.ui.overlayutil.DrivingRouteOverlay;
import com.weimore.caringhelper.utils.SmsUtils;
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
    private RoutePlanSearch mSearch = null;
    private BDLocation mCurLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_demo);
        initView();
    }

    public static void startActivity(Context context, SuggestionResult.SuggestionInfo suggestionInfo) {
        Intent intent = new Intent(context, MapDemoActivity.class);
        intent.putExtra("suggestionInfo", suggestionInfo);
        context.startActivity(intent);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MapDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SuggestionResult.SuggestionInfo si = intent.getParcelableExtra("suggestionInfo");
        if (si != null) {
            if (choosePos == 0) {
                mBinding.tvOrigin.setText(si.key);
                updateMapLocation(si.getPt());
            } else {
                mBinding.tvDestination.setText(si.key);
            }
        }
        if (!TextUtils.isEmpty(mBinding.tvOrigin.getText()) && !TextUtils.isEmpty(mBinding.tvDestination.getText())) {
            PlanNode stNode = PlanNode.withCityNameAndPlaceName(mCity, mBinding.tvOrigin.getText().toString());
            PlanNode enNode = PlanNode.withCityNameAndPlaceName(mCity, mBinding.tvDestination.getText().toString());
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
    }

    private void initView() {
        initMap();
        mBinding.tvOrigin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mCity)) {
                choosePos = 0;
                MapSearchActivity.startActivity(this, mCity);
            }
        });
        mBinding.tvDestination.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mCity)) {
                choosePos = 1;
                MapSearchActivity.startActivity(this, mCity);
            }
        });
        mBinding.tvReloc.setOnClickListener(v -> {
            mLocationClient.restart();
            mBinding.tvDestination.setText("");
        });
        mBinding.tvSms.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBinding.tvOrigin.getText()) || "".equals(mBinding.tvOrigin.getText().toString().trim())) {
                ToastUtil.showShort("请选择起始地");
            }
            if (TextUtils.isEmpty(mBinding.tvDestination.getText()) || "".equals(mBinding.tvDestination.getText().toString().trim())) {
                ToastUtil.showShort("请选择目的地");
            }
            if(mCurLocation==null){
                ToastUtil.showShort("定位失败，请确认是否开启网络或GPS,并重新定位");
                return;
            }
            SmsUtils.sendMessage(this,"18255039301",
                    SmsUtils.addressContent(mCurLocation.getAddrStr()+"," + mCurLocation.getLocationDescribe()));
        });
    }

    /**
     * 初始化地图配置
     */
    private void initMap() {
        mBaiduMap = mBinding.map.getMap();
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        BitmapDescriptor currentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, currentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
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
        mLocationClient.start();

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                //获取驾车线路规划结果
//                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
//                overlay.setData(result.getRouteLines().get(0));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
//        mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            int errorCode = location.getLocType(); //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            List<Poi> poiList = location.getPoiList(); //获取周边POI信息
            updateMapLocation(location);
            L.d(locationDescribe);
            L.d(new Gson().toJson(poiList));
            if (mLocationClient != null) {
                mLocationClient.stop();
            }
        }
    }

    private void updateMapLocation(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mCurLocation = location;
        mCity = location.getCity();
        mBinding.tvOrigin.setText(location.getStreet());
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
        }
    }

    private void updateMapLocation(LatLng latLng) {
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latLng.latitude)
                .longitude(latLng.longitude).build();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationEnabled(false);
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
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mBinding.map.onDestroy();
        mSearch.destroy();
    }
}
