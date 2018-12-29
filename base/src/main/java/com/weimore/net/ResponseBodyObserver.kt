package com.weimore.net

import com.weimore.util.L
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author Weimore
2018/12/6.
description:
 */
abstract class ResponseBodyObserver<T,R>(var back: BaseCallback<R>?=null): BaseObserver<T>(null) {


    override fun onError(throwable: Throwable) {
        L.e(throwable.toString())
        back?.error(throwable.message)
    }

}