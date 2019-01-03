package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.config.ConstantUrl;
import com.weimore.caringhelper.entity.User;
import com.weimore.caringhelper.net.OkHttpUtil;
import com.weimore.caringhelper.ui.activity.LoginActivity;
import com.weimore.caringhelper.ui.contract.LoginContract;
import com.weimore.caringhelper.ui.model.LoginModel;
import com.weimore.entity.Result;
import com.weimore.net.BaseCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    public LoginPresenter(@Nullable LoginContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public LoginContract.Model getModel() {
        return new LoginModel();
    }

    @Override
    public void login(String phone, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("phoneNo",phone);
        map.put("password",password);
        OkHttpUtil.get(ConstantUrl.LOGIN, map,User.class, new BaseCallback<User>() {
            @Override
            public void callback(User data) {
                if(isNotDetach()){
                    ((LoginActivity)getMView()).loginSuccess(data);
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {

            }
        });
    }
}