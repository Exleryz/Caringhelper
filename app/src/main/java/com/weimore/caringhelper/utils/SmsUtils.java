package com.weimore.caringhelper.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.weimore.util.PermissionUtil;
import com.weimore.util.ToastUtil;

import java.util.List;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class SmsUtils {

    /**
     * 发送短信
     *
     * @param tel     电话号码
     * @param content 短息内容
     */
    public static void sendMessage(Context context, String tel, String content) {
        PermissionUtil.permissionRequest(context, () -> {

            Intent sendIntent = new Intent("SENT_SMS_ACTION");
            PendingIntent sendPI = PendingIntent.getBroadcast(context, 0, sendIntent, 0);

            SmsManager smsManager = SmsManager.getDefault();
            List<String> divideContents = smsManager.divideMessage(content);
            for (String text : divideContents) {
                smsManager.sendTextMessage(tel, null, text, sendPI, null);
            }
            ToastUtil.showShort("短信发送成功");
        }, Manifest.permission.SEND_SMS);

    }

    public static String addressContent(String address){
        return "我当前位置处于" + address + "";
    }


}
