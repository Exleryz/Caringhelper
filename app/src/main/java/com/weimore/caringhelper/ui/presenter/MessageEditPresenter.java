package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.ui.contract.MessageEditContract;
import com.weimore.caringhelper.ui.model.MessageEditModel;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class MessageEditPresenter extends BasePresenter<MessageEditContract.View, MessageEditContract.Model> implements MessageEditContract.Presenter {

    public MessageEditPresenter(@Nullable MessageEditContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public MessageEditContract.Model getModel() {
        return new MessageEditModel();
    }

}