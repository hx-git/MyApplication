package com.cali.common.kt

import com.blankj.rxbus.RxBus
import com.cali.common.constant.EventObj
import com.cali.common.constant.EventStr


fun <T> T?.registerEventSimple(tag: String, block:(it: EventStr) -> Unit) {
    this?.apply {
        RxBus.getDefault().subscribe<EventStr>(this, tag) {
            block(it)
        }
    }
}

fun <T,O> T?.registerEventObj(tag: String, block:(it:O) -> Unit) {
    this?.apply {
        RxBus.getDefault().subscribe<EventObj<O>>(this, tag) {
            block(it.obj)
        }
    }
}



fun <T> T?.unregister() {
    RxBus.getDefault().unregister(this)
}


fun EventStr.post() {
    RxBus.getDefault().post(this,this.msg)
}


fun <T> EventObj<T>.post() {
    RxBus.getDefault().post(this,this.msg)
}








