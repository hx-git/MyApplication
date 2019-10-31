package com.cali.libcore.util.preference

import android.content.Context
import android.content.SharedPreferences
import com.cali.libcore.base.ContextHolder
import com.cali.libcore.base.SP_DEFAULT_NAME
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object StringPreference :ReadWriteProperty<Any?,String>{
    private val prefs: SharedPreferences by lazy {
        ContextHolder.application.getSharedPreferences(SP_DEFAULT_NAME, Context.MODE_PRIVATE)
    }

    //使用属性名作为键值
    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return prefs.getString(property.name,"")?:""
    }

    //使用属性名作为键值
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        prefs.edit().putString(property.name,value)
                .apply()
    }
}