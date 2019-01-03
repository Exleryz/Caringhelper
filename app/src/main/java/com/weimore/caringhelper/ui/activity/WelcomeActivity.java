package com.weimore.caringhelper.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.ui.contract.WelcomeContract;
import com.weimore.caringhelper.ui.presenter.WelcomePresenter;
import com.weimore.util.PermissionUtil;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class WelcomeActivity extends BaseActivity<WelcomeContract.Presenter> implements WelcomeContract.View {

    private Handler mHandler = new Handler();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @NonNull
    @Override
    public WelcomeContract.Presenter getPresenter() {
        return new WelcomePresenter(this);
    }

    @Override
    public void initView() {
        super.initView();
        //先申请必要权限
        PermissionUtil.permissionRequest(this, this::start, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void start() {
        mHandler.postDelayed(() -> {
            if (ConfigKey.ifLogin()) {
                //如果有缓存数据，则无需登录
                MapDemoActivity.startActivity(WelcomeActivity.this);
            } else {
                LoginActivity.startActivity(WelcomeActivity.this);
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.permissionRequest(this, this::start, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}