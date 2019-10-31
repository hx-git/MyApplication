package com.cali.libcore.kt

import android.os.Bundle
import com.cali.common.kt.toast
import com.cali.libcore.view.compose.BabushkaText
import com.cali.libcore.base.ContextHolder

const val BUNDLE_TAG = "bundle_tag"
fun String.bundle(): Bundle {
    val bundle = Bundle()
    bundle.putString(BUNDLE_TAG,this)
    return bundle
}

fun Bundle?.string():String{
    return this?.getString(BUNDLE_TAG)?:"default"
}

//多颜色文本拼接
fun String.pieceBuild(color: Int, textSize: Float): BabushkaText.Piece{
    return BabushkaText.Piece.Builder(this)
        .textColor(color)
        .textSizeRelative(textSize)
        .build()
}

fun String.toast() {
    ContextHolder.application.apply {
        toast(this@toast)
    }
}