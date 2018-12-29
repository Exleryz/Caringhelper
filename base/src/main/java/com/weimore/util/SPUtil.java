package com.weimore.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.weimore.base.BaseApplication;

import java.util.HashMap;


public class SPUtil {

    /**
     * 添加数据到sharedPreference
     */
    public static synchronized void put(String key, Object value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (null == value) {
            editor.putString(key, "");
        } else {
            editor.putString(key, value.toString());
        }

        editor.apply();
    }

    public static int getInt(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext()).getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext()).getBoolean(key, defaultValue);
    }

    public static float getFloat(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext()).getFloat(key, 0);
    }

    public static long getLong(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext()).getLong(key, 0L);
    }

    public static String getString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext()).getString(key, "");
    }

    /**
     * 判断某个key是否存在
     */
    public static boolean contains(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        return sharedPreferences.contains(key);
    }

    /**
     * 删除某个键值对
     */
    public static void remove(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * 清空所有数据
     */
    public static void clear() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 清空 用户当前账号 数据
     */
    public static void clearUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        if (sharedPreferences.contains(ConstantSpKey.UserInfo.USER_ID)) {
//            editor.remove(ConstantSpKey.UserInfo.USER_ID);
//        }
//
//        if (sharedPreferences.contains(ConstantSpKey.UserInfo.MOBILE)) {
//            editor.remove(ConstantSpKey.UserInfo.MOBILE);
//        }
//
//        if (sharedPreferences.contains(ConstantSpKey.UserInfo.PASSWORD)) {
//            editor.remove(ConstantSpKey.UserInfo.PASSWORD);
//        }


        editor.apply();
    }

    /**
     * 返回所有键值对
     */
    public static HashMap<String, ?> getAll() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.Companion.getContext());
        return (HashMap<String, ?>) sharedPreferences.getAll();
    }
}
