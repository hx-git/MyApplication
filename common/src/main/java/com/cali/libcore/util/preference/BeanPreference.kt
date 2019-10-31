package com.cali.libcore.util.preference

import android.content.Context
import android.content.SharedPreferences
import com.cali.common.constant.EventSimple
import com.cali.common.kt.post
import com.cali.libcore.base.ContextHolder
import com.cali.libcore.base.SP_DEFAULT_NAME
import com.cali.libcore.util.GsonUtils
import com.zchu.log.Logger
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BeanPreference<T:Any>(private val bean:T):ReadWriteProperty<Any?,T>{
    private val prefs: SharedPreferences by lazy {
        ContextHolder.application.getSharedPreferences(SP_DEFAULT_NAME, Context.MODE_PRIVATE)
    }

    //使用属性名作为键值
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val values :String = prefs.getString(property.name, "")?:""
        Logger.i("BeanPreference-getValue:${property.name}-$values")
        val toBean :T? = GsonUtils.instance.toBean(values,bean.javaClass)
//        return toBean?:bean.javaClass.newInstance()
        return toBean?:bean
    }

    //使用属性名作为键值
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Logger.i("BeanPreference-setValue:$value")
        prefs.edit()
            .putString(property.name,GsonUtils.instance.toString(value))
            .apply()
        //保存完再发送event事件
        EventSimple(property.name).post()
    }
}