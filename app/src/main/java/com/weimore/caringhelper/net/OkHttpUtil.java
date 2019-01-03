package com.weimore.caringhelper.net;

import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.weimore.entity.Result;
import com.weimore.net.BaseCallback;
import com.weimore.net.HttpConfig;
import com.weimore.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Weimore
 *         2018/12/29.
 *         description:
 */

public class OkHttpUtil {

    private static final class Holder {
        final static OkHttpClient INSTANCE = HttpConfig.createOkHttpClient();
    }

    private static OkHttpClient getInstance() {
        return Holder.INSTANCE;
    }

    private static Request.Builder getRequestBuilder(String url) {
        return new Request.Builder().url(url);
    }

    public static <T> Result<T> get(String url, Map<String, Object> mapValue, Class<T> returnClz, BaseCallback<T> callback) {

        final Request request = getRequestBuilder(url + createParams(mapValue)).get().build();
        getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.getMainLooper().getQueue().addIdleHandler(() -> {
                    ToastUtil.showShort("网络连接失败");
                    return false;
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!call.isCanceled() && call.isExecuted() && response.body() != null) {
                    Looper.getMainLooper().getQueue().addIdleHandler(() -> {
                        try {
                            Method method = OkHttpUtil.class.getMethod("get", String.class, Map.class, Class.class, BaseCallback.class);
                            Type type = method.getGenericReturnType();
                            Gson gson = new Gson();
                            TypeAdapter<Result<T>> adapter = gson.getAdapter((TypeToken<Result<T>>) TypeToken.get(type));
                            JsonReader jsonReader = gson.newJsonReader(response.body().charStream());
                            Result<T> result = adapter.read(jsonReader);
                            if (result.getFlag()) {
                                callback.callback((T) new Gson().fromJson(result.getData().toString(), returnClz));
                            } else {
                                ToastUtil.showShort(result.getMsg());
                                callback.error(result.getMsg());
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return false;
                    });
                } else {
                    callback.error(response.message());
                }

            }
        });
        return null;
    }

    private static String createParams(Map<String, Object> mapValue) {
        if (mapValue == null) {
            throw new RuntimeException("map 不能为空");
        }
        StringBuilder builder = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return builder.substring(0, builder.length() - 1);
    }

}
