package com.cali.libcore.util.preference

import android.content.Context
import android.content.SharedPreferences
import com.cali.libcore.base.ContextHolder
import com.cali.libcore.base.SP_DEFAULT_NAME
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T:Any>(val name: String = SP_DEFAULT_NAME, private val default: T)
    :ReadWriteProperty<Any?,T>{
    private val prefs: SharedPreferences by lazy {
        ContextHolder.application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    //使用属性名作为键值
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(property.name, default)
    }

    //使用属性名作为键值
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(property.name, value)
    }

    private fun putSharePreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }.apply()
    }

    private fun getSharePreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, 0)
            is String -> getString(name, "")
            is Int -> getInt(name, 0)
            is Boolean -> getBoolean(name, false)
            is Float -> getFloat(name, 0f)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }
        return res as T
    }
}