package com.weimore.util;

import android.util.TypedValue;

import com.weimore.base.BaseApplication;

/**
 * @author Weimore
 *         2018/1/12.
 *         description:
 */

public class DimenUtil {

    /**
     * 得到屏幕宽度
     */
    public static int getScreenWidth() {
        return BaseApplication.Companion.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到屏幕高度
     */
    public static int getScreenHeight() {
        return BaseApplication.Companion.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 把dp转换成px
     */
    public static int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                BaseApplication.Companion.getContext().getResources().getDisplayMetrics());
    }

    /**
     * 把sp转换成px
     */
    public static int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp,
                BaseApplication.Companion.getContext().getResources().getDisplayMetrics());
    }
}
