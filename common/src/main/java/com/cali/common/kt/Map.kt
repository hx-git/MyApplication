package com.cali.common.kt


inline fun <reified T> T.toMap(): HashMap<String, String> {
    val clazz = T::class.java
    val map = hashMapOf<String,String>()
    clazz.declaredFields.forEach {
        it.isAccessible = true
        val value = it.get(this).toString()
        if (value.isNotEmpty()) {
            map[it.name] = value
        }
    }
    return map
}