package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.ui.contract.AppSettingContract;
import com.weimore.caringhelper.ui.presenter.AppSettingPresenter;

/**
 * @author Weimore
 *         2018/12/29.
 *         description:
 */

public class AppSettingActivity extends BaseActivity<AppSettingContract.Presenter> implements AppSettingContract.View {



    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AppSettingActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public boolean dataBinding() {
        DataBindingUtil.setContentView(this,R.layout.activity_app_setting);
        return true;
    }

    @NonNull
    @Override
    public AppSettingContract.Presenter getPresenter() {
        return new AppSettingPresenter(this);
    }

}