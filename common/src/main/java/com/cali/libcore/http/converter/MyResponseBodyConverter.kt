package com.cali.libcore.http.converter

import com.cali.common.kt.debugLogInfo
import com.google.gson.Gson

import java.io.IOException
import java.lang.reflect.Type

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Gson响应体变换器
 *
 */
internal class MyResponseBodyConverter<T>(private val gson: Gson, private val type: Type) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val response = value.string()
        "返回信息---converter:$response".debugLogInfo()
        return gson.fromJson<T>(response, type)
    }
}