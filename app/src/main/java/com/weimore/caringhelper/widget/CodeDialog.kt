package com.weimore.caringhelper.widget

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AppCompatDialog
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.weimore.caringhelper.R
import com.weimore.caringhelper.utils.callback.MyCallback
import com.weimore.util.DimenUtil
import com.weimore.util.KeyBoardUtil
import com.weimore.util.ToastUtil
import kotlinx.android.synthetic.main.dialog_code.*

/**
 * @author Weimore
2018/12/7.
description:
 */
class CodeDialog(context: Context?, theme :Int= R.style.WhiteBackGroundDialog) : AppCompatDialog(context, theme) {

    private var mCode:String = ""
    private var mBitmap:Bitmap? = null

    init {
        setContentView(R.layout.dialog_code)
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

    fun setData(bitmap: Bitmap,code:String){
        mCode =code
        setCodeImage(bitmap)
    }

    private fun setCodeImage(bitmap: Bitmap){
        if(mBitmap!=null){
            mBitmap!!.recycle()
        }
        mBitmap = bitmap;
//        Glide.with(context).load(bitmap).into(im_code)
        im_code.setImageBitmap(mBitmap)
    }

    fun setConfirmListener(callback: MyCallback<Boolean>){
        tv_confirm.setOnClickListener {
            if(TextUtils.isEmpty(getText()) || getText().trim().equals("")){
                ToastUtil.showShort("请输入验证码")
                return@setOnClickListener
            }
            if(!getText().toLowerCase().equals(mCode.toLowerCase())){
                ToastUtil.showShort("验证码错误")
                return@setOnClickListener
            }
            callback.callback(true)
        }
    }

    fun setImageListener(listener:View.OnClickListener){
        im_code.setOnClickListener(listener)
    }

    fun getText():String{
        return tv_edit.text.toString()
    }

    override fun dismiss() {
        tv_edit.text.clear()
        KeyBoardUtil.hideKeyboard(tv_edit)
        super.dismiss()
    }

}