package com.weimore.caringhelper.ui.receiver;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.weimore.caringhelper.base.MyApplication;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.dao.helper.ContactBeanHelper;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.ui.activity.MainGroupActivity;
import com.weimore.caringhelper.utils.SmsUtils;
import com.weimore.util.L;
import com.weimore.util.SPUtil;
import com.yanzhenjie.permission.AndPermission;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //去电
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            L.d("call OUT:" + phoneNumber);
        } else {
            //来电
            if (!AndPermission.hasPermission(MyApplication.Companion.getContext(), Manifest.permission.READ_PHONE_STATE)) {
                return;
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (tm == null) {
                return;
            }
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        }
    }


    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    L.d("挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    L.d("接听");
                case TelephonyManager.CALL_STATE_RINGING:
                    L.d("响铃:来电号码" + incomingNumber);
                    Contact contact = ContactBeanHelper.queryOneByPhone(incomingNumber);
                    if(contact==null || !SPUtil.getBoolean(ConfigKey.AUTO_SMS)){
                        return;
                    }
                    MainGroupActivity.instance.getLocation(location -> {
                        if (!TextUtils.isEmpty(MainGroupActivity.getAddress())) {
                            L.d("发送短信");
                            SmsUtils.sendMessage(MyApplication.Companion.getContext(),incomingNumber,MainGroupActivity.getAddress());
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
}

