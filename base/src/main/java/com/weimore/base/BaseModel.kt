package com.weimore.base

import com.weimore.entity.Result
import com.weimore.net.CatchExceptionFunction
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @author Weimore
2018/12/24.
description:
 */
open class BaseModel: IBaseModel {


    override fun <T> changeThread(observable: Observable<Result<T>>, observer: Observer<T>) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(CatchExceptionFunction<T>())
                .subscribe(observer) }


    override fun <T> changeIOThread(observable: Observable<Response<T>>, observer: Observer<Response<T>>) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

}