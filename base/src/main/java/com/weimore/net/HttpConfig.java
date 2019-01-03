package com.weimore.net;

import com.weimore.net.interceptor.JsonInterceptor;
import com.weimore.net.interceptor.MoreBaseUrlInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author Weimore
 *         2018/11/17.
 *         description: 网络配置类
 */

public class HttpConfig {

    private static final int CONNECT_TIME_OUT = 10 * 1000;
    private static final int IO_TIME_OUT = 10 * 1000;

    public static OkHttpClient createOkHttpClient(){
        return new OkHttpClient.Builder()
//                .addInterceptor(new MoreBaseUrlInterceptor())
                .addInterceptor(new JsonInterceptor())
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(IO_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(IO_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();

    }



}
