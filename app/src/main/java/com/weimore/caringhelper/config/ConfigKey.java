package com.weimore.caringhelper.config;

import android.text.TextUtils;

import com.weimore.util.SPUtil;

import retrofit2.http.PUT;

/**
 * @author Weimore
 *         2018/12/30.
 *         description:
 */

public class ConfigKey {

    public final static String FIRST_INIT = "first_init";

    public final static String AUTO_SMS = "auto_send_message";

    public final static String MESSAGE_MODE_A = "message_mode_a";

    public final static String MESSAGE_MODE_B = "message_mode_b";


    public interface UserInfo {

        String USER_ID = "user_id";

        String USER_NAME = "user_name";

        String PHONE = "phone";

        String CREATE_TIME = "create_time";

    }

    public static boolean ifLogin() {
        return !TextUtils.isEmpty(getPhoneNumber());
    }

    public static void setUserId(String userId) {
        SPUtil.put(UserInfo.USER_ID, userId);
    }

    public static String getUserId() {
        return SPUtil.getString(UserInfo.USER_ID);
    }

    public static void setPhoneNumber(String phoneNumber) {
        SPUtil.put(UserInfo.PHONE, phoneNumber);
    }

    public static String getPhoneNumber() {
        return SPUtil.getString(UserInfo.PHONE);
    }

    public static void setUserName(String userName) {
        SPUtil.put(UserInfo.USER_NAME, userName);
    }

    public static String getUserName() {
        return SPUtil.getString(UserInfo.USER_NAME);
    }

    public static void setCreateTime(String createTime) {
        SPUtil.put(UserInfo.CREATE_TIME, createTime);
    }

    public static String getCreateTime() {
        return SPUtil.getString(UserInfo.CREATE_TIME);
    }

    public static String getSmsModeA() {
        return SPUtil.getString(MESSAGE_MODE_A);
    }

    public static void setSmsModeA(String modeString) {
        SPUtil.put(MESSAGE_MODE_A, modeString);
    }

    public static String getSmsModeB() {
        return SPUtil.getString(MESSAGE_MODE_B);
    }

    public static void setSmsModeB(String modeString) {
        SPUtil.put(MESSAGE_MODE_B, modeString);
    }

    public static void clearInfo() {
        setUserId("");
        setUserName("");
        setPhoneNumber("");
        setCreateTime("");
    }

}
