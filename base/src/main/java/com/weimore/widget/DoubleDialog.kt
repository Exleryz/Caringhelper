package com.weimore.widget

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.weimore.base.R
import com.weimore.util.DimenUtil
import kotlinx.android.synthetic.main.dialog_double.*

/**
 * @author Weimore
2018/11/22.
description:
 */
class DoubleDialog : AppCompatDialog {

    init {
        setContentView(R.layout.dialog_double)
        val lp = window!!.attributes
        lp.width = (DimenUtil.getScreenWidth() * 0.7).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        window!!.attributes = lp
        initView()
    }

    constructor(context: Context) : this(context, R.style.ThemeBackGroundDialog) {

    }

    constructor(context: Context, theme: Int) : super(context, theme) {

    }

    private fun initView() {
        tv_cancel.setOnClickListener { dismiss() };
    }

    fun confirmListener(listener: View.OnClickListener) {
        tv_confirm.setOnClickListener(listener)
    }

    fun cancelListener(listener: View.OnClickListener) {
        tv_cancel.setOnClickListener(listener)
    }

    fun confirmText(text: String) {
        tv_confirm.text = text
    }

    fun cancelText(text: String) {
        tv_cancel.text = text
    }

    fun setContent(content: String) {
        tv_content.text = content
    }

    fun setTitle(title: String) = {
        im_code.text = title
    }

    fun hideTitle() = {
        im_code.visibility = View.GONE
    }

}