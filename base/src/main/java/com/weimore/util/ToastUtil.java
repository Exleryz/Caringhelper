package com.weimore.util;

import android.widget.Toast;

import com.weimore.base.BaseApplication;

/**
 * @author Weimore
 *         2018/11/17.
 *         description:
 */

public class ToastUtil {

    public static Toast mToast = Toast.makeText(BaseApplication.Companion.getContext(),"",Toast.LENGTH_SHORT);


    public static void showShort(String content){
        mToast.setText(content);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLong(String content){
        mToast.setText(content);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }


}
