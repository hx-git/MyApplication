package com.cali.libcore.http

import com.cali.libcore.BuildConfig

/**
 * 时间：2019/5/31 10:49
 * 描述：
 */
object HttpConfig {
    const val KEY = "key"
    const val KEY_MAP = "fb0a1b0d89f3b93adca639f0a29dbf23"
    val BASE_URL = if(BuildConfig.DEBUG)
//        "http://192.168.102.2:8086/comment/"
        "https://asas.ayus18.n4cmsp.cn/"
    else
        "https://asas.ayus18.n4cmsp.cn/"

    //备用地址
    const val SPARE_BASE_URL = "http://192.168.102.2:8086/"
}