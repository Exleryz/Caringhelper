package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.ui.contract.WelcomeContract;
import com.weimore.caringhelper.ui.model.WelcomeModel;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public class WelcomePresenter extends BasePresenter<WelcomeContract.View, WelcomeContract.Model> implements WelcomeContract.Presenter {

    public WelcomePresenter(@Nullable WelcomeContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public WelcomeContract.Model getModel() {
        return new WelcomeModel();
    }

}