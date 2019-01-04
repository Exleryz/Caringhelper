package com.weimore.caringhelper.net;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.weimore.caringhelper.utils.JsonUtils;
import com.weimore.entity.Result;
import com.weimore.net.BaseCallback;
import com.weimore.net.HttpConfig;
import com.weimore.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
                            Charset charset = response.body().contentType().charset(Charset.forName("UTF-8"));
                            Method method = OkHttpUtil.class.getMethod("get", String.class, Map.class, Class.class, BaseCallback.class);
                            Type type = method.getGenericReturnType();
                            Gson gson = new Gson();
//                            TypeAdapter<Result<T>> adapter = gson.getAdapter((TypeToken<Result<T>>) TypeToken.get(type));
//                            JsonReader jsonReader = gson.newJsonReader(response.body().charStream());
//                            Result<T> result = adapter.read(jsonReader);
                            Result<T> result = gson.fromJson(response.body().source().buffer().readString(charset),type);
                            if (result.getFlag()) {
                                callback.callback((T) new Gson().fromJson(gson.toJson(result.getData()), returnClz));
                            } else {
                                ToastUtil.showShort(result.getMsg());
                                callback.error(result.getMsg());
                            }
                        } catch (Exception e) {
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


    public static <T,K> Result<T> post(String url, Object object, Class<K> returnClz, BaseCallback<T> callback) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),gson.toJson(object));
        final Request request = getRequestBuilder(url).post(requestBody).build();
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
                            Charset charset = response.body().contentType().charset(Charset.forName("UTF-8"));
                            Method method = OkHttpUtil.class.getMethod("post", String.class, Object.class, Class.class, BaseCallback.class);
                            Type type = method.getGenericReturnType();
                            Gson gson = new Gson();
//                            TypeAdapter<Result<T>> adapter = gson.getAdapter((TypeToken<Result<T>>) TypeToken.get(type));
//                            JsonReader jsonReader = gson.newJsonReader(response.body().source().buffer().readString(charset));
//                            Result<T> result = adapter.read(jsonReader);
                            Result<T> result = gson.fromJson(response.body().source().buffer().readString(charset),type);
                            if (result.getFlag()) {
                                if(JsonUtils.isJsonArray(result.getData().toString())){
                                    K k = gson.fromJson(gson.toJson(result.getData()),returnClz);
                                    T t = gson.fromJson(gson.toJson(k), new TypeToken<T>() {}.getType());
                                    callback.callback(t);
                                }else if(result.getData() instanceof JSONArray){
                                    callback.callback((T) new Gson().fromJson(gson.toJson(result.getData()), returnClz));
                                }
                            } else {
                                ToastUtil.showShort(result.getMsg());
                                callback.error(result.getMsg());
                            }
                        } catch (Exception e) {
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

}
