package com.weimore.base

import android.content.Intent

/**
 * @author Weimore
2018/11/16.
description:
 */
interface IBasePresenter{

    fun detach()

    fun getIntent(intent: Intent) {

    }

}