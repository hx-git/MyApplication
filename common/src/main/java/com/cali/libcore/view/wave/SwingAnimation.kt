package com.cali.libcore.view.wave

import android.view.animation.Animation
import android.view.animation.Transformation

class SwingAnimation(private var waveTimes:Int, private var waveRange:Int):Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        //实现周期
        val d = Math.sin(interpolatedTime * Math.PI * waveTimes) * waveRange
        t?.matrix?.setTranslate(d.toFloat(),0f)
    }
}