package com.weimore.caringhelper.config;

import android.text.TextUtils;

import com.weimore.util.SPUtil;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class ConfigKey {

    public final static String AUTO_SMS = "auto_send_message";

    public interface UserInfo{

        String USER_ID = "user_id";

        String USER_NAME = "user_name";

        String PHONE = "phone";

    }

    public static boolean ifLogin(){
        return !TextUtils.isEmpty(getPhoneNumber());
    }

    public static void setUserId(String userId){
        SPUtil.put(UserInfo.USER_ID,userId);
    }

    public static String getUserId(){
        return SPUtil.getString(UserInfo.USER_ID);
    }

    public static void setPhoneNumber(String phoneNumber){
        SPUtil.put(UserInfo.PHONE,phoneNumber);
    }

    public static String getPhoneNumber(){
        return SPUtil.getString(UserInfo.PHONE);
    }

    public static void setUserName(String userName){
        SPUtil.put(UserInfo.USER_NAME,userName);
    }

    public static String getUserName(){
        return SPUtil.getString(UserInfo.USER_NAME);
    }

    public static void clearInfo(){
        setUserId("");
        setUserName("");
        setPhoneNumber("");
    }

}
