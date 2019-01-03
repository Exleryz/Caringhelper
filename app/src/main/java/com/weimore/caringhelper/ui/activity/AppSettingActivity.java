package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.databinding.ActivityAppSettingBinding;
import com.weimore.caringhelper.ui.contract.AppSettingContract;
import com.weimore.caringhelper.ui.presenter.AppSettingPresenter;
import com.weimore.util.SPUtil;

/**
 * @author Weimore
 *         2018/12/29.
 *         description: APP设置界面
 */

public class AppSettingActivity extends BaseActivity<AppSettingContract.Presenter> implements AppSettingContract.View {

    private ActivityAppSettingBinding mBinding;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AppSettingActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_setting);
        return true;
    }

    @NonNull
    @Override
    public AppSettingContract.Presenter getPresenter() {
        return new AppSettingPresenter(this);
    }

    @Override
    public void initView() {
        mBinding.toolbar.hideLeftImage();
        mBinding.swAutoSms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SPUtil.put(ConfigKey.AUTO_SMS, isChecked);
        });
        mBinding.tvChangePassword.setOnClickListener(v -> {
            ChangePasswordActivity.startActivity(this);
        });
        mBinding.tvExit.setOnClickListener(v -> {
            LoginActivity.startActivity(this);
        });
    }
}