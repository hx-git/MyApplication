package com.cali.libcore.http.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class MyConverterFactory(val gson: Gson): Converter.Factory() {
    fun create(): MyConverterFactory {
        return create(Gson())
    }

    fun create(gson: Gson): MyConverterFactory {
        return MyConverterFactory(gson)
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        //返回我们自定义的Gson响应体变换器
        return MyResponseBodyConverter<Type>(gson, type)
    }

    override fun requestBodyConverter(type: Type,
                             parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        //返回我们自定义的Gson请求体变换器
        val adapter = gson.getAdapter(TypeToken.get(type))
        return MyRequestBodyConverter(gson, adapter)
    }
}