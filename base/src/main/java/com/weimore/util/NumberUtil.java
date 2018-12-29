package com.weimore.util;

import android.text.TextUtils;

/**
 * @author Weimore
 *         2018/12/11.
 *         description:
 */

public class NumberUtil {

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (number.length() != 11) {
            return false;
        }
        if (!(number.substring(0, 1).equals("1"))) {
            return false;
        }
        String sec = number.substring(1, 2);
        if (sec.equals("1") || sec.equals("2")) {
            return false;
        }
        return true;

    }

}
