package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.ui.contract.AppSettingContract;
import com.weimore.caringhelper.ui.model.AppSettingModel;

/**
 * @author Weimore
 *         2018/12/29.
 *         description:
 */

public class AppSettingPresenter extends BasePresenter<AppSettingContract.View, AppSettingContract.Model> implements AppSettingContract.Presenter {

    public AppSettingPresenter(@Nullable AppSettingContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public AppSettingContract.Model getModel() {
        return new AppSettingModel();
    }

}