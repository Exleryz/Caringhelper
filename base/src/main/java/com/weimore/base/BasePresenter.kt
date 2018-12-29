package com.weimore.base


import com.weimore.net.BaseCallback
import com.weimore.util.ToastUtil

/**
 * @author Weimore
2018/11/16.
description:
 */
abstract class BasePresenter<V : IBaseView, M : IBaseModel>(var mView: V?) : IBasePresenter {

    protected var mModel: M

    init {
        mModel = getModel()
    }

    override fun detach() {
        mView = null
    }

    fun isNotDetach(): Boolean = mView != null

    abstract fun getModel(): M


    inner abstract class PresenterCallback<T>:BaseCallback<T>{

        override fun error(message:String?) {
            mView?.dismissLoading()
            ToastUtil.showShort(message?:"")
        }

    }


}