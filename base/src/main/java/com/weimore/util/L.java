package com.weimore.util;

import android.util.Log;

import com.weimore.base.BuildConfig;

/**
 * @author Weimore
 *         2018/11/17.
 *         description: 日志类
 */

public class L {

    public static final String TAG = "HTTP";

//    private static boolean OPEN_LOG = BuildConfig.DEBUG;

    private static boolean OPEN_LOG = true;

    public static void e(String message){
        if(OPEN_LOG){
            Log.e(TAG,message);
        }
    }

    public static void d(String message){
        if(OPEN_LOG){
            Log.d(TAG,message);
        }
    }

}
