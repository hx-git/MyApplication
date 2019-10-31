package com.cali.libcore.util

@Retention(AnnotationRetention.RUNTIME)//存储在编译后的 Class 文件，反射可见。
@Target(AnnotationTarget.FUNCTION)//方法（不包括构造函数）
annotation class MethodCheckLogin

@Retention(AnnotationRetention.RUNTIME)//存储在编译后的 Class 文件，反射可见。
@Target(AnnotationTarget.FUNCTION)//方法（不包括构造函数）
annotation class MethodCheckUserId

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class IntegralRequest

