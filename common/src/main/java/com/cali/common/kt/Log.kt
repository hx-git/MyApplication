package com.cali.common.kt

import com.cali.common.BuildConfig
import com.zchu.log.Logger

//log调用栈信息
inline fun <T> T.debugLogInfo(block:()-> String = {"debugLogInfo"}) {
    val message = this.toString()
    if (BuildConfig.DEBUG) {
        val stackTrace = Thread.currentThread().stackTrace
        stackTrace?.apply {
            if (this.size >= 5) {
                stackTrace[2]?.apply {
                    val fileName = fileName.substringBeforeLast(".")
                    Logger.d("${block()}:$fileName-$methodName\n${block()}:$message")
                }
            }
        }
    }
}