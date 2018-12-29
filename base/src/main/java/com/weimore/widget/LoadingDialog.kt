package com.weimore.widget

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Gravity
import android.view.WindowManager
import com.weimore.base.R
import com.weimore.util.DimenUtil

/**
 * @author Weimore
2018/11/16.
description:
 */
class LoadingDialog(context: Context) : AppCompatDialog(context, R.style.WhiteBackGroundDialog) {


    init {
        setContentView(R.layout.dialog_loading)
        val lp = window!!.attributes
        lp.width = (DimenUtil.getScreenWidth() * 0.7).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        window!!.attributes = lp
    }


}