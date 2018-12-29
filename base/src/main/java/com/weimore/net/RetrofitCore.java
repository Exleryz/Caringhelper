package com.weimore.net;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Weimore
 *         2018/6/12.
 *         description:
 */

public class RetrofitCore {

    private Retrofit mRetrofit;

    private RetrofitCore() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(MyUrl.NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(HttpConfig.createOkHttpClient())
                .build();
    }

    private static final class RetrofitCoreHolder {
        private final static RetrofitCore INSTANCE = new RetrofitCore();
    }

    private static RetrofitCore getInstance() {
        return RetrofitCoreHolder.INSTANCE;
    }

    public static <T> T init(Class<T> service) {
        return getInstance().mRetrofit.create(service);
    }

}
