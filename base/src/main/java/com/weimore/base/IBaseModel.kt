package com.weimore.base

import com.weimore.entity.Result
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.Response

/**
 * @author Weimore
2018/11/16.
description:
 */
interface IBaseModel{

    /**
     * 通过Rx切换线程
     */
    fun <T> changeThread(observable: Observable<Result<T>>, observer: Observer<T>)

    fun <T> changeIOThread(observable: Observable<Response<T>>, observer: Observer<Response<T>>)

}