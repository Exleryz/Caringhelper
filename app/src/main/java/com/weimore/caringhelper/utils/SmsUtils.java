package com.weimore.caringhelper.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;
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

    public static String addressContent(BDLocation location){
        return "我当前位置处于" + location.getCity()+location.getDistrict()+location.getStreet() + "," + location.getLocationDescribe();
    }

    public static String addressContent(BDLocation location, SuggestionResult.SuggestionInfo suggestionInfo){
        LatLng point1 = new LatLng(location.getLatitude(),location.getLongitude());
        LatLng point2 = suggestionInfo.getPt();
        String distance = String.format("%.2f", DistanceUtil.getDistance(point1,point2)/1000) + " km";
        return addressContent(location) + "。" + "距离目的地:" + suggestionInfo.city +suggestionInfo.district + suggestionInfo.key + "还有约"+ distance + "。";
    }


}
