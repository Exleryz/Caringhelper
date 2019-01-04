package com.weimore.caringhelper.ui.activity;

import android.Manifest;
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
import com.weimore.util.PermissionUtil;
import com.weimore.util.SPUtil;
import com.yanzhenjie.permission.AndPermission;

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
        mBinding.swAutoSms.setChecked(SPUtil.getBoolean(ConfigKey.AUTO_SMS));
        mBinding.swAutoSms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //判断是否有发送短信的权限
                if (!AndPermission.hasPermission(AppSettingActivity.this, Manifest.permission.SEND_SMS)) {
                    buttonView.setChecked(false);
                    PermissionUtil.permissionRequest(AppSettingActivity.this, () -> {
                                buttonView.setChecked(true);
                                SPUtil.put(ConfigKey.AUTO_SMS, true);
                            },
                            Manifest.permission.SEND_SMS);
                }else {
                    SPUtil.put(ConfigKey.AUTO_SMS, true);
                }
            } else {
                SPUtil.put(ConfigKey.AUTO_SMS, false);
            }
        });
        mBinding.layoutEdit.setOnClickListener(v->{
            MessageEditActivity.startActivity(this);
        });
        mBinding.tvChangePassword.setOnClickListener(v -> {
            ChangePasswordActivity.startActivity(this);
        });
        mBinding.tvExit.setOnClickListener(v -> {
            LoginActivity.startActivity(this);
        });
    }

    @Override
    public void initData() {
        mBinding.tvName.setText(ConfigKey.getUserName());
        mBinding.tvPhone.setText(ConfigKey.getPhoneNumber());
        mBinding.tvCreateTime.setText(ConfigKey.getCreateTime());
    }
}