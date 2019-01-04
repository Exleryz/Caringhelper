package com.weimore.caringhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.R;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.databinding.ActivityMessageEditBinding;
import com.weimore.caringhelper.ui.contract.MessageEditContract;
import com.weimore.caringhelper.ui.presenter.MessageEditPresenter;
import com.weimore.util.SPUtil;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class MessageEditActivity extends BaseActivity<MessageEditContract.Presenter> implements MessageEditContract.View {

    private ActivityMessageEditBinding mBinding;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MessageEditActivity.class));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_message_edit;
    }

    @Override
    public boolean dataBinding() {
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_message_edit);
        return true;
    }

    @NonNull
    @Override
    public MessageEditContract.Presenter getPresenter() {
        return new MessageEditPresenter(this);
    }

    @Override
    public void initData() {
        mBinding.etModeA.setText(ConfigKey.getSmsModeA());
        mBinding.etModeB.setText(ConfigKey.getSmsModeB());
        mBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mBinding.etModeA.getText()) || TextUtils.isEmpty(mBinding.etModeB.getText())){
                    showToast("短信内容不能为空");
                    return;
                }
                if(!mBinding.etModeA.getText().toString().contains("A")){
                    showToast("模式1格式中未包含当前位置(A)");
                    return;
                }
                if(!mBinding.etModeB.getText().toString().contains("A")){
                    showToast("模式2格式中未包含当前位置(A)");
                    return;
                }
                if(!mBinding.etModeB.getText().toString().contains("B")){
                    showToast("模式2格式中未包含目的地(B)");
                    return;
                }
                ConfigKey.setSmsModeA(mBinding.etModeA.getText().toString());
                ConfigKey.setSmsModeB(mBinding.etModeB.getText().toString());
                showToast("保存成功");
                finish();
            }
        });
    }
}