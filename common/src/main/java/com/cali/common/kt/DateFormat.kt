package com.cali.common.kt

import java.text.SimpleDateFormat
import java.util.*

//默认24小时制
val timePattern = "yyyy-MM-dd"
val timePatternWithH = "yyyy-MM-dd HH:mm:ss"



//明天
fun String.titleTomorrow(): String? {
    val sdf = SimpleDateFormat(this, Locale.CHINA)
    return sdf.format(Date(System.currentTimeMillis()).add(1))
}

//今天
fun String.titleToday(): String? {
    val sdf = SimpleDateFormat(this, Locale.CHINA)
    return sdf.format(Date(System.currentTimeMillis()))
}

//昨天
fun String.titleYesterday(): String? {
    val sdf = SimpleDateFormat(this, Locale.CHINA)
    return sdf.format(Date(System.currentTimeMillis()).add(-1))
}

fun Date.add(num: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE,num)
    return calendar.time
}

//到零点的时间
fun timeToZero(): Long{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    //加一天
    calendar.add(Calendar.DATE,1)
    calendar.set(Calendar.HOUR_OF_DAY,0)
    calendar.set(Calendar.MINUTE,0)
    calendar.set(Calendar.SECOND,0)
    calendar.set(Calendar.MILLISECOND,0)
    return calendar.timeInMillis - System.currentTimeMillis()
}

/**
 * 如果是今天 只显示 HH:mm:ss
 */
fun Long.timeMillisFormat():String{
    val date = Date(this)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    val format = sdf.format(date)
    return if (format.contains(todayDay())) {
        format.split(" ")[1]
    }else{
        format
    }
}

fun todayDay():String{
    val date = Date(System.currentTimeMillis())
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    return sdf.format(date)
}