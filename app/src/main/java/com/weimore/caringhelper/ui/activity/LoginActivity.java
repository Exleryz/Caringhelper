package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.databinding.ActivityLoginBinding;
import com.weimore.caringhelper.entity.User;
import com.weimore.caringhelper.ui.contract.LoginContract;
import com.weimore.caringhelper.ui.presenter.LoginPresenter;
import com.weimore.util.NumberUtil;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private ActivityLoginBinding mBinding;
    private long currentTime;

    public static void startActivity(Context context) {
        ConfigKey.clearInfo();
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        return true;
    }

    @NonNull
    @Override
    public LoginContract.Presenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void initView() {
        mBinding.toolbar.hideLeftImage();
        mBinding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    mBinding.imPhoneDelete.setVisibility(View.VISIBLE);
                }else {
                    mBinding.imPhoneDelete.setVisibility(View.GONE);
                }
            }
        });
        mBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    mBinding.imPasswordDelete.setVisibility(View.VISIBLE);
                }else {
                    mBinding.imPasswordDelete.setVisibility(View.GONE);
                }
            }
        });
        mBinding.imPhoneDelete.setOnClickListener(v->{
            mBinding.etPhone.setText("");
        });
        mBinding.imPasswordDelete.setOnClickListener(v->{
            mBinding.etPassword.setText("");
        });
        mBinding.tvLogin.setOnClickListener(v->{
            if(TextUtils.isEmpty(mBinding.etPhone.getText())){
                showToast("请输入手机号");
                return;
            }
            if(!NumberUtil.isMobile(mBinding.etPhone.getText().toString())){
                showToast("请输入正确的手机号");
                return;
            }
            if(TextUtils.isEmpty(mBinding.etPassword.getText())){
                showToast("请输入密码");
                return;
            }
            showLoading();
            getMPresenter().login(mBinding.etPhone.getText().toString(),mBinding.etPassword.getText().toString());
        });
        mBinding.tvRegister.setOnClickListener(v->{
            RegisterActivity.startActivity(LoginActivity.this);
        });
    }

    @Override
    public void loginSuccess(User user) {
        dismissLoading();
        ConfigKey.setPhoneNumber(user.getPhoneNo());
        ConfigKey.setUserName(user.getUserName());
        MapDemoActivity.startActivity(this);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime < 2000) {
                finish();
            } else {
                currentTime = System.currentTimeMillis();
                showToast("再按一次退出程序");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}