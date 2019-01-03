package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
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
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMapBinding;
import com.weimore.caringhelper.utils.SmsUtils;
import com.weimore.util.ToastUtil;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class MapActivity extends AppCompatActivity {

    private ActivityMapBinding mBinding;
    private BaiduMap mBaiduMap;

    private String mCity;
    private int choosePos = 0;
    private RoutePlanSearch mSearch = null;
    private BDLocation mCurLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        initView();
    }

    public static void startActivity(Context context, SuggestionResult.SuggestionInfo suggestionInfo) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("suggestionInfo", suggestionInfo);
        context.startActivity(intent);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
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
        mBinding.tvDestination.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mCity)) {
                choosePos = 1;
                MapSearchActivity.startActivity(this, mCity);
            }
        });
        mBinding.tvReloc.setOnClickListener(v -> {
            MainGroupActivity parent = (MainGroupActivity) getParent();
            if (parent != null) {
                parent.getCurrentLocation(this::updateMapLocation);
            }
            mBinding.tvDestination.setText("");
        });
        mBinding.tvSms.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBinding.tvOrigin.getText()) || "".equals(mBinding.tvOrigin.getText().toString().trim())) {
                ToastUtil.showShort("请选择起始地");
            }
            if (TextUtils.isEmpty(mBinding.tvDestination.getText()) || "".equals(mBinding.tvDestination.getText().toString().trim())) {
                ToastUtil.showShort("请选择目的地");
            }
            if (mCurLocation == null) {
                ToastUtil.showShort("定位失败，请确认是否开启网络或GPS,并重新定位");
                return;
            }
            SmsUtils.sendMessage(this, "18255039301",
                    SmsUtils.addressContent(mCurLocation.getAddrStr() + "," + mCurLocation.getLocationDescribe()));
        });
        mBinding.map.postDelayed(() -> {
            MainGroupActivity parent = (MainGroupActivity) getParent();
            if (parent != null) {
                parent.getCurrentLocation(MapActivity.this::updateMapLocation);
            }
        },500);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void updateMapLocation(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mCurLocation = location;
        mCity = location.getCity();
        mBinding.tvOrigin.setText(String.format("%s%s%s", location.getCity(), location.getDistrict(), location.getStreet()));
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
        mBaiduMap.setMyLocationEnabled(false);
        mBinding.map.onDestroy();
        mSearch.destroy();
    }
}
