package com.weimore.caringhelper.utils;

import android.text.TextUtils;

/**
 * @author Weimore
 *         2019/1/5.
 *         description:
 */

public class JsonUtils {

    public static boolean isJsonArray(String str) {
       if (TextUtils.isEmpty(str)){
           return false;
       }
       if("[".equals(str.substring(0, 1))){
           return true;
       }
       return false;
    }

}
