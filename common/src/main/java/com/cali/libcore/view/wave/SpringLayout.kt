package com.cali.libcore.view.wave

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.LayoutInflater
import android.view.VelocityTracker
import android.view.View
import android.widget.LinearLayout
import androidx.dynamicanimation.animation.SpringAnimation
import com.cali.common.kt.gone
import com.cali.common.R
import kotlinx.android.synthetic.main.spring_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpringLayout(context:Context, attrs:AttributeSet?, def:Int):LinearLayout(context,attrs,def) {

    init {
        orientation = VERTICAL
        gravity = CENTER_HORIZONTAL
        //加载视图
        val springView:View = LayoutInflater.from(context)
            .inflate(R.layout.spring_item_layout,this)
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SpringLayout, 0, 0)
        attrArray?.apply {
            springView.tv_num.text = attrArray.getString(R.styleable.SpringLayout_spring_inner_text)
            if (attrArray.getString(R.styleable.SpringLayout_spring_tv_label)?.isEmpty() == true) {
                springView.tv_label.gone()
            }else{
                springView.tv_label.text = attrArray.getString(R.styleable.SpringLayout_spring_tv_label)
            }

            springView.iv.setImageResource(attrArray.getResourceId(R.styleable.SpringLayout_spring_inner_src,0))
        }
        attrArray?.recycle()
    }

    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)

    private val springAnim :SpringAnimation by lazy {
        SpringAnimation(this,SpringAnimation.TRANSLATION_Y,1f)
    }

    fun startSpringAnimation(time:Long = 0): SpringLayout {
        GlobalScope.launch(Dispatchers.Main){
            delay(time)
            springAnim.startSwing()
        }
        return this
    }

    private fun SpringAnimation.startSwing() {
        spring.stiffness = 1f //SpringForce.STIFFNESS_VERY_LOW
        spring.dampingRatio = 0f //SpringForce.DAMPING_RATIO_LOW_BOUNCY  不会停止
        setStartVelocity(50f) //起始位置  不能小于50f
        start()
    }

    fun onDestroy() {
        springAnim.cancel()
    }

}