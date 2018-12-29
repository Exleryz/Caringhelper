package com.weimore.net.interceptor

import com.weimore.util.L
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 * @author Weimore
2018/12/6.
description:
 */
class MoreBaseUrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val UTF8 = Charset.forName("UTF-8")
        //获取原始的originalRequest
        val originalRequest = chain.request()
        //获取老的url
        val oldUrl = originalRequest.url()
        var body: String? = null

        val requestBody = originalRequest.body()
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset)
        }

//        L.d("请求\nurl：" + oldUrl + "\nbody:" + body)
        //获取originalRequest的创建者builder
        val builder = originalRequest.newBuilder()
        //获取头信息的集合如：manage,mdffx
        val urlnameList = originalRequest.headers("baseUrl")
        if (urlnameList != null && urlnameList.size == 1) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader("baseUrl")
            //获取头信息中配置的value
            val baseUrl = urlnameList[0]
            val baseURL = HttpUrl.parse(baseUrl.replace("baseUrl:", baseUrl))
            //重建新的HttpUrl，需要重新设置的url部分
            val newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL!!.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build()
            //获取处理后的新newRequest
            val newRequest = builder.url(newHttpUrl).build()
            L.d( "new_url: " + newRequest.url())
            return chain.proceed(newRequest)
        } else {
            return chain.proceed(originalRequest)
        }

    }


}