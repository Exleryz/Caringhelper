package com.weimore.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author Weimore
 *         2018/12/12.
 *         description:
 */

public class KeyBoardUtil {

    /**
     * 显示软键盘
     */
    public static void showKeyboard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.setCursorVisible(true);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.setCursorVisible(false);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * 切换软键盘状态
     */
    public static void toggleSoftInput(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        if (imm != null) {
            imm.toggleSoftInput(0, 0);
        }
    }

}
