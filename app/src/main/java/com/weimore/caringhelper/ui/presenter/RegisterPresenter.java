package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.config.ConstantUrl;
import com.weimore.caringhelper.net.OkHttpUtil;
import com.weimore.caringhelper.ui.contract.RegisterContract;
import com.weimore.caringhelper.ui.model.RegisterModel;
import com.weimore.entity.Result;
import com.weimore.net.BaseCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View, RegisterContract.Model> implements RegisterContract.Presenter {

    public RegisterPresenter(@Nullable RegisterContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public RegisterContract.Model getModel() {
        return new RegisterModel();
    }

    @Override
    public void register(String phoneNo, String userName, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("phoneNo",phoneNo);
        map.put("userName",userName);
        map.put("password",password);
        OkHttpUtil.get(ConstantUrl.REGISTER, map,String.class, new BaseCallback<String>() {
            @Override
            public void callback(String data) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                    getMView().registerSuccess(data);
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                    getMView().showToast(message);
                }
            }
        });
    }


}