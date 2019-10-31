package com.cali.libcore.http.interceptor

import com.cali.common.kt.debugLogInfo
import com.cali.libcore.http.HttpConfig.SPARE_BASE_URL
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException
import java.io.InterruptedIOException

class RetryInterceptor internal constructor(builder: Builder) : Interceptor {
    var executionCount: Int = 0      // 最大重试次数
    /**
     * retry间隔时间
     */
    val retryInterval: Long     // 重试的间隔

    init {
        this.executionCount = builder.executionCount
        this.retryInterval = builder.retryInterval
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response = chain.proceed(request)
        "请求:${response.isSuccessful}".debugLogInfo()
        var retryNum = 0
        while ((!response.isSuccessful) && retryNum <= executionCount) {
            val nextInterval = retryInterval
            try {
                Thread.sleep(nextInterval)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw InterruptedIOException()
            }
            retryNum++
            response = chain.proceed(request)
        }
        "重试次数:$retryNum success:${response.isSuccessful}".debugLogInfo()
        //没成功 并且到重试次数，更换baseUrl
//        if (!response.isSuccessful && retryNum > executionCount) {
//            HttpUrl.parse(SPARE_BASE_URL)?.apply {
//                val builder = request.newBuilder()
//                val newFullUrl = request.url()
//                    .newBuilder()
//                    .scheme(scheme())
//                    .host(host())
//                    .port(port())
//                    .build()
//                response = chain.proceed(builder.url(newFullUrl).build())
//            }
//        }
        return response
    }

    class Builder {
        var executionCount: Int = 0
        var retryInterval: Long = 0

        init {
            executionCount = 2
            retryInterval = 1000
        }

        fun executionCount(executionCount: Int): Builder {
            this.executionCount = executionCount
            return this
        }

        fun retryInterval(retryInterval: Long): Builder {
            this.retryInterval = retryInterval
            return this
        }

        fun build(): RetryInterceptor {
            return RetryInterceptor(this)
        }
    }
}
