package com.weimore.base

import android.content.Context
import com.weimore.util.ToastUtil

/**
 * @author Weimore
2018/11/16.
description:
 */
interface IBaseView {

    fun getContext():Context?

    fun showToast(content:String){
        ToastUtil.showShort(content)
    }

    fun showLoading()

    fun dismissLoading()

}