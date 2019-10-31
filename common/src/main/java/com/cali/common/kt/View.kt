package com.cali.common.kt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.cali.common.R
import kotlinx.coroutines.*


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.goneWithAnim() {
    val hideAnim = TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
        Animation.RELATIVE_TO_SELF,2.0f,
        Animation.RELATIVE_TO_SELF, 0.0f,
        Animation.RELATIVE_TO_SELF,0.0f)
    hideAnim.duration = 300
    this.startAnimation(hideAnim)
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.createBitmap(): Bitmap{
    //height - 2 消除view生成的黑边
    val bitmap = Bitmap.createBitmap(width,height - 2,Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    layout(0,0,width,height)
    draw(canvas)
    return bitmap
}

/**
 * n次点击事件
 */
const val COUNTS = 4
var hits = LongArray(COUNTS)
fun continuousClick(count: Int = COUNTS, time: Long = 1000, block:()->Unit) {
    System.arraycopy(hits,1, hits,0, hits.size - 1)
    hits[hits.size - 1] = SystemClock.uptimeMillis()
    if (hits[0] >= (SystemClock.uptimeMillis() - time)) {
        hits = LongArray(count)
        block()
    }
}

/**
 * 倒计时
 */
fun TextView.countDown(time: Int, text: String): Job {
    val countTv = this
    return GlobalScope.launch(Dispatchers.Main){
        countTv.isClickable = false
        for (i in time downTo 0) {
            countTv.text = String.format(countTv.context.resources.getString(R.string.count_content),i)
            delay(1000)
        }
        countTv.isClickable = true
        countTv.text = text
    }
}