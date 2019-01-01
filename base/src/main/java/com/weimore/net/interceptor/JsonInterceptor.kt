package com.weimore.net.interceptor

import android.util.Log
import com.google.gson.Gson
import com.weimore.util.L
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * @author Weimore
2018/12/6.
description:
 */
class JsonInterceptor: Interceptor {

    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestBody = request.body()
        var body: String? = null

        if (requestBody != null ) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset)
        }

        L.d("请求:\nUrl:[ " + request.url() + " ]\nBody:[ " + body +" ]")

        val newRequest = request.newBuilder().addHeader("Content-Type","application/json;charset=utf-8").build()
//        val newRequest = request.newBuilder().addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8").build()
        val response = chain.proceed(newRequest)
//        val response = chain.proceed(request)

        val responseBody = response.body()
        var rBody: String? = null

        if (responseBody != null) {
            val source = responseBody.source()
            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source?.buffer()
            var charset = UTF8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }
            rBody = buffer?.clone()?.readString(charset)
            buffer?.close()
        }
        try {
            L.d("响应:\n" + Gson().toJson(rBody).replace("\\", ""))
        }catch (e:Exception){
            L.e("解析失败\n")
        }

        return response
    }
}