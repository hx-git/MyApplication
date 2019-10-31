package com.cali.common.constant

//首页等待时间
const val DEFAULT_PAGE_SIZE = "10"

//Event
data class EventStr(val msg:String, val info:String = "")
data class EventObj<O>(val msg:String, val obj:O)










