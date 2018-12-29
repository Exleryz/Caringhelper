package com.weimore.caringhelper.net

import com.weimore.net.RetrofitCore

/**
 * @author Weimore
2018/12/6.
description:
 */
class MyService {

    companion object {

        fun init():Api = RetrofitCore.init(Api::class.java)

    }

}