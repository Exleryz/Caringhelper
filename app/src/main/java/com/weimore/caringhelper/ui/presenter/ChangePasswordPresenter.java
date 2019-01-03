package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.config.ConstantUrl;
import com.weimore.caringhelper.entity.User;
import com.weimore.caringhelper.net.OkHttpUtil;
import com.weimore.caringhelper.ui.contract.ChangePasswordContract;
import com.weimore.caringhelper.ui.model.ChangePasswordModel;
import com.weimore.entity.Result;
import com.weimore.net.BaseCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordContract.View, ChangePasswordContract.Model> implements ChangePasswordContract.Presenter {

    public ChangePasswordPresenter(@Nullable ChangePasswordContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public ChangePasswordContract.Model getModel() {
        return new ChangePasswordModel();
    }

    @Override
    public void reLogin(String phone, String oldPassword) {
        Map<String,Object> map = new HashMap<>();
        map.put("phoneNo",phone);
        map.put("password",oldPassword);
        OkHttpUtil.get(ConstantUrl.LOGIN, map, User.class,new BaseCallback<User>() {
            @Override
            public void callback(User data) {
                if(isNotDetach()){
                    getMView().reLoginSuccess();
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }
        });
    }

    @Override
    public void updatePassword(String phone, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("phoneNo",phone);
        map.put("password",password);
        OkHttpUtil.get(ConstantUrl.UPDATE_PASSWORD, map, User.class,new BaseCallback<User>() {
            @Override
            public void callback(User data) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                    getMView().showToast("密码修改成功");
                    getMView().updateSuccess();
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }
        });
    }
}