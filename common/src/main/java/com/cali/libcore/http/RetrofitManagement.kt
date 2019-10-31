package com.cali.libcore.http

import com.cali.common.BuildConfig
import com.cali.libcore.base.ContextHolder
import com.cali.libcore.http.converter.MyConverterFactory
import com.google.gson.Gson

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 作者：leavesC
 * 时间：2019/5/31 11:18
 * 描述：
 */
class RetrofitManagement private constructor() {

    companion object {

        private const val READ_TIMEOUT: Long = 10000

        private const val WRITE_TIMEOUT: Long = 10000

        private const val CONNECT_TIMEOUT: Long = 10000

        val instance: RetrofitManagement by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManagement()
        }

    }

    private val serviceMap = ConcurrentHashMap<String, Any>()

    private fun createRetrofit(url: String): Retrofit {
        val builder = OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
//                .retryOnConnectionFailure(true)
//                .addInterceptor(FilterInterceptor())
//                .dns(HttpDns())
                .sslSocketFactory(MySSL.createSSLSocketFactory(), MySSL.TrustAllManager())
                .hostnameVerifier(MySSL.TrustAllHostNameVerifier())

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //重试拦截器
//        builder.addInterceptor(RetryInterceptor.Builder()
//            .build())
        //debug模式下添加拦截器
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
//            builder.addInterceptor(MonitorInterceptor(ContextHolder.application))
        }
        //添加公共参数
//        builder.addInterceptor(ParamsInterceptor(ContextHolder.application))


        val client = builder.build()
        val retrofitBuilder = Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        if (BuildConfig.DEBUG) {
            retrofitBuilder.addConverterFactory(MyConverterFactory(Gson()))
        } else {
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        }

        return retrofitBuilder.build()
    }

    //默认base_url
    fun <T : Any> getService(clz: Class<T>, host: String = HttpConfig.BASE_URL): T {
        if (serviceMap.containsKey(host)) {
            val obj = serviceMap[host] as? T
            obj?.let {
                return obj
            }
        }
        val value = createRetrofit(host).create(clz)
        serviceMap[host] = value
        return value
    }

}