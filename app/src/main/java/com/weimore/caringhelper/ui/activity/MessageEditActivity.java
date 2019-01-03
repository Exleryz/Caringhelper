package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.ui.contract.MessageEditContract;
import com.weimore.caringhelper.ui.presenter.MessageEditPresenter;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class MessageEditActivity extends BaseActivity<MessageEditContract.Presenter> implements MessageEditContract.View {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MessageEditActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_message_edit;
    }

    @NonNull
    @Override
    public MessageEditContract.Presenter getPresenter() {
        return new MessageEditPresenter(this);
    }

}