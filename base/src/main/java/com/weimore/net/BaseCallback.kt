package com.weimore.net

/**
 * @author Weimore
2018/12/6.
description:
 */
interface BaseCallback<T> {

    fun callback(data:T)

    fun error(message:String?)

}