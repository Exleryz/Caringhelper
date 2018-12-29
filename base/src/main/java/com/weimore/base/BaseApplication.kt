package com.weimore.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex


/**
 * @author Weimore
2018/11/16.
description:
 */
open class BaseApplication:Application(){


    companion object {
        private lateinit var mAppContext:Context

        fun getContext():Context{
            return mAppContext
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this

    }

}