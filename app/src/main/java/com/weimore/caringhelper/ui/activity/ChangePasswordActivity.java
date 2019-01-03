package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.databinding.ActivityChangePasswordBinding;
import com.weimore.caringhelper.ui.contract.ChangePasswordContract;
import com.weimore.caringhelper.ui.presenter.ChangePasswordPresenter;
import com.weimore.util.NumberUtil;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class ChangePasswordActivity extends BaseActivity<ChangePasswordContract.Presenter> implements ChangePasswordContract.View {

    private ActivityChangePasswordBinding mBinding;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChangePasswordActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_change_password;
    }

    @NonNull
    @Override
    public ChangePasswordContract.Presenter getPresenter() {
        return new ChangePasswordPresenter(this);
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        return true;
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
            if (TextUtils.isEmpty(mBinding.etOldPassword.getText())) {
                showToast("昵称不能为空");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etNewPassword.getText())) {
                showToast("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etPasswordConfirm.getText())) {
                showToast("请输入验证密码");
                return;
            }
            if (!TextUtils.equals(mBinding.etNewPassword.getText(), mBinding.etPasswordConfirm.getText())) {
                showToast("新密码输入不一致");
                return;
            }
            showLoading();
            getMPresenter().reLogin(mBinding.etPhone.getText().toString(),
                    mBinding.etOldPassword.getText().toString());

        });

    }

    @Override
    public void reLoginSuccess() {
        getMPresenter().updatePassword(mBinding.etPhone.getText().toString(),
                mBinding.etNewPassword.getText().toString());
    }

    @Override
    public void updateSuccess() {
        finish();
    }
}