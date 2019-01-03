package com.weimore.widget

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.weimore.base.R
import com.weimore.util.DimenUtil
import kotlinx.android.synthetic.main.dialog_edittext.*

/**
 * @author Weimore
2019/1/3.
description:
 */
/**
 * @author Weimore
2019/1/3.
description:
 */
class EditDialog(context: Context) : AppCompatDialog(context, R.style.WhiteBackGroundDialog) {

    init {
        setContentView(R.layout.dialog_edittext)
        val lp = window!!.attributes
        lp.width = (DimenUtil.getScreenWidth() * 0.7).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        window!!.attributes = lp
        initView()
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

    fun setTitleText(title: String) = {
        im_code.visibility = View.VISIBLE
        im_code.text = title
    }

    fun hideTitle() = {
        im_code.visibility = View.GONE
    }

    fun getNameText(): String {
        return et_name.text.toString()
    }

    fun setNameAndPhone(name:String, phone:String){
        et_name.setText(name)
        et_phone.setText(phone)
    }


    override fun dismiss() {
        super.dismiss()
        et_name.text.clear()
        et_phone.text.clear()
    }

    fun getPhoneText():String{
        return et_phone.text.toString()
    }

}