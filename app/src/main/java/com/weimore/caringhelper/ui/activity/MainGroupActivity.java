package com.weimore.caringhelper.ui.activity;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityMainGroupBinding;
import com.weimore.caringhelper.utils.SmsUtils;
import com.weimore.util.L;
import com.weimore.util.ToastUtil;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class MainGroupActivity extends ActivityGroup {

    private ActivityMainGroupBinding mBinding;
    private long currentTime;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private static String locationStr = "";
    private static String destinationStr = "";
    private BDLocation mCurLocation;
    private List<LocationCallback> callbackList = new ArrayList<>();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainGroupActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_group);
        initView();
        initLocation();
    }

    private void initLocation() {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (mBinding.navigation.getSelectedItemId()) {
            case R.id.navigation_map:
                switchActivity("map", MapActivity.class);
                break;
            case R.id.navigation_contact:
                switchActivity("contact", ContactActivity.class);
                break;
            case R.id.navigation_mine:
                switchActivity("mine", AppSettingActivity.class);
                break;
            default:
                break;
        }
        mLocationClient.restart();
    }

    private void initView() {
        mBinding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        switchActivity("map", MapActivity.class);
                        break;
                    case R.id.navigation_contact:
                        switchActivity("contact", ContactActivity.class);
                        break;
                    case R.id.navigation_mine:
                        switchActivity("mine", AppSettingActivity.class);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    /**
     * 切换界面
     */
    private void switchActivity(String activityId, Class<?> clz) {
        mBinding.container.removeAllViews();
        Intent intent = new Intent(this, clz);
        mBinding.container.addView(getLocalActivityManager().startActivity(activityId,
                intent).getDecorView(),
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime < 2000) {
                System.exit(0);
            } else {
                currentTime = System.currentTimeMillis();
                ToastUtil.showShort("再按一次退出程序");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    public static void setLocationStr(BDLocation location) {
        locationStr = SmsUtils.addressContent(location.getAddrStr() + "," + location.getLocationDescribe());
    }

    public static void setDestinationStr(String addr) {
        destinationStr = addr;
    }

    public static String getAddress() {
        return TextUtils.isEmpty(locationStr) ? "" : locationStr + (TextUtils.isEmpty(destinationStr) ? "" : destinationStr);
    }

    public void getCurrentLocation(@NotNull LocationCallback callback) {
        if (mCurLocation != null) {
            callback.callback(mCurLocation);
        } else {
            if (!callbackList.contains(callback)) {
                callbackList.add(callback);
            }
        }
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
            mCurLocation = location;
            if (callbackList != null) {
                for (LocationCallback locationCallback : callbackList) {
                    locationCallback.callback(mCurLocation);
                }
                callbackList.clear();
            }
            setLocationStr(location);
            L.d(locationDescribe);
            L.d(new Gson().toJson(poiList));
            if (mLocationClient != null) {
                mLocationClient.stop();
            }
        }
    }

    public interface LocationCallback {

        void callback(BDLocation location);
    }
}