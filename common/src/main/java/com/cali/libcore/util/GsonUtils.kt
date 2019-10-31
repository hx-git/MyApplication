package com.cali.libcore.util

import com.google.gson.Gson

import java.lang.reflect.Type


/**
 * Created by Dinosa on 2018/1/31.
 */

class GsonUtils private constructor() {
    private val gson: Gson by lazy {
        Gson()
    }

    fun toString(obj: Any): String {
        return gson.toJson(obj)
    }

    fun <T> toBean(str: String, clazz: Class<T>): T {
        return gson.fromJson(str, clazz)
    }

    fun <T> toListBean(str: String, clazz: Type): T? {
        return gson.fromJson<T>(str, clazz)
    }

    companion object {
        private lateinit var gsonUtils: GsonUtils
        val instance: GsonUtils
            get() {
                synchronized(GsonUtils::class.java) {
                    gsonUtils = GsonUtils()
                }
                return gsonUtils
            }
    }


}
