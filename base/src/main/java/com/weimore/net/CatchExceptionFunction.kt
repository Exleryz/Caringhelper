package com.weimore.net

import android.util.Log
import com.weimore.entity.Result
import com.weimore.util.ToastUtil
import io.reactivex.functions.Function

/**
 * @author Weimore
2018/12/6.
description:
 */
class CatchExceptionFunction<T> : Function<Result<T>, T>{

    @Throws(Exception::class)
    override fun apply(baseResult: Result<T>): T? {
        if (baseResult.flag) {
            throw Exception(baseResult.msg)
        } else {
            return baseResult.data;
        }
    }

}