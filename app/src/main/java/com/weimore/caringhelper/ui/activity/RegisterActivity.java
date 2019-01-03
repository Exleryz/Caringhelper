package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityRegisterBinding;
import com.weimore.caringhelper.ui.contract.RegisterContract;
import com.weimore.caringhelper.ui.presenter.RegisterPresenter;
import com.weimore.util.NumberUtil;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {

    private ActivityRegisterBinding mBinding;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        return true;
    }

    @NonNull
    @Override
    public RegisterContract.Presenter getPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void initView() {
        mBinding.tvRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBinding.etPhone.getText())) {
                showToast("请输入手机号");
                return;
            }
            if (!NumberUtil.isMobile(mBinding.etPhone.getText().toString())) {
                showToast("请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etUsername.getText())) {
                showToast("昵称不能为空");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etPassword.getText())) {
                showToast("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etPasswordConfirm.getText())) {
                showToast("请验证密码");
                return;
            }
            if (!TextUtils.equals(mBinding.etPassword.getText(), mBinding.etPasswordConfirm.getText())) {
                showToast("密码输入不一致");
                return;
            }
            showLoading();
            getMPresenter().register(mBinding.etPhone.getText().toString(),
                    mBinding.etUsername.getText().toString(),
                    mBinding.etPassword.getText().toString()
            );

        });

    }

    @Override
    public void registerSuccess(String msg) {
        showToast(msg);
        finish();
    }
}