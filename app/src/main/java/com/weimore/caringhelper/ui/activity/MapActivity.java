package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

    private final static int CHOOSE_DESTINATION = 123;

    private ActivityMapBinding mBinding;
    private BaiduMap mBaiduMap;
    private String mCity;
    private RoutePlanSearch mSearch = null;
    private BDLocation mCurLocation;
    private SuggestionResult.SuggestionInfo mSuggestionInfo;

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
            mBinding.tvDestination.setText(si.key);
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
                Intent intent = new Intent(this,MapSearchActivity.class);
                intent.putExtra("city",mCity);
                startActivityForResult(intent,CHOOSE_DESTINATION);
            }
        });
        mBinding.tvReloc.setOnClickListener(v -> {
            MainGroupActivity parent = (MainGroupActivity) getParent();
            if (parent != null) {
                parent.getLocation(location -> {
                    mCurLocation = location;
                    updateMapLocation(mCurLocation,mSuggestionInfo);
                });
            }
        });
        mBinding.tvClear.setOnClickListener(v -> {
            mSuggestionInfo = null;
            mBinding.tvDestination.setText("");
            MainGroupActivity.setDestination(null);
            updateMapLocation(mCurLocation,mSuggestionInfo);
        });
        mBinding.map.postDelayed(() -> {
            MainGroupActivity parent = (MainGroupActivity) getParent();
            if (parent != null) {
                parent.getLocation(MapActivity.this::updateMapLocation);
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

    private void updateMapLocation(BDLocation location) {
        updateMapLocation(location,null);
    }

    private void updateMapLocation(BDLocation location, SuggestionResult.SuggestionInfo suggestionInfo) {
        mBaiduMap.clear();
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
        if(suggestionInfo!=null){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);

    }

    private void updateMapLocation(LatLng latLng) {
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
        mBaiduMap.setMyLocationEnabled(false);
        mBinding.map.onDestroy();
        mSearch.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == CHOOSE_DESTINATION){
            //选择目的地返回
            if(data!=null){
                mSuggestionInfo = data.getParcelableExtra("suggestion");
                if(mSuggestionInfo!=null){
                    MainGroupActivity.setDestination(mSuggestionInfo);
                    mBinding.tvDestination.setText(
                            String.format("%s%s%s", mSuggestionInfo.city, mSuggestionInfo.district, mSuggestionInfo.key));
                    updateMapLocation(mCurLocation,mSuggestionInfo);
                }
            }
        }
    }
}
