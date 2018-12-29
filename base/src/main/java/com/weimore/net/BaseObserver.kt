package com.weimore.net

import com.weimore.base.BuildConfig
import com.weimore.util.L
import com.weimore.util.ToastUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @author Weimore
2018/12/6.
description:
 */
abstract class BaseObserver<T>(var callback: BaseCallback<T>?=null): Observer<T> {

    private var mDisposable:Disposable?=null

    override fun onSubscribe(disposable: Disposable) {
        mDisposable = disposable;
    }

    override fun onError(throwable: Throwable) {
        L.e(throwable.toString())
        if(throwable is SocketTimeoutException || throwable is ConnectException){
            callback?.error("网络连接不稳定~")
        }else{
            callback?.error(throwable.message)
        }
        if(mDisposable?.isDisposed != true){
            mDisposable?.dispose()
        }
    }

    override fun onComplete() {
        if(mDisposable?.isDisposed != true){
            mDisposable?.dispose()
        }
    }
}