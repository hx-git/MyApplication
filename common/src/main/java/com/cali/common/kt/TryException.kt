package com.cali.common.kt

import java.io.Closeable

fun doWithTry(block:()->Unit) {
    try {
        block()
    } catch (e: Exception) {

    }
}

fun <T> T.doWithTryReturn(block: () -> Unit): Boolean {
    try {
        block()
    } catch (e: Exception) {
        return false
    }
    return true
}

fun  Closeable?.deWithTry(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {

    }finally {
        this?.close()
    }
}