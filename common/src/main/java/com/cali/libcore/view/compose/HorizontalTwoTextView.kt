package com.cali.libcore.view.compose

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER_VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.cali.common.R
import kotlinx.android.synthetic.main.include_horizontal_two_tv.view.*

class HorizontalTwoTextView(context: Context, val attrs: AttributeSet?, def: Int) : LinearLayout(context, attrs, def) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    private var twoTvLayout:View
    init {
        orientation = HORIZONTAL
        gravity = CENTER_VERTICAL
        //加载视图
        twoTvLayout = LayoutInflater.from(context)
            .inflate(R.layout.include_horizontal_two_tv, this)
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.HorizontalTwoTextView, 0, 0)
        attrArray?.apply {
            twoTvLayout.tvLeft.text = attrArray.getString(R.styleable.HorizontalTwoTextView_tv_left_text)
            twoTvLayout.tvRight.text = attrArray.getString(R.styleable.HorizontalTwoTextView_tv_right_text)
            val tvLeftColor = attrArray.getColor(R.styleable.HorizontalTwoTextView_tv_left_text_color, 0)
            val tvRightColor = attrArray.getColor(R.styleable.HorizontalTwoTextView_tv_right_text_color, 0)
            val tvLeftSize = attrArray.getDimension(R.styleable.HorizontalTwoTextView_tv_left_text_size,0f)
            val tvRightSize = attrArray.getDimension(R.styleable.HorizontalTwoTextView_tv_right_text_size,0f)
            if(tvLeftSize!=0f) twoTvLayout.tvLeft.textSize = tvLeftSize
            if(tvRightSize!=0f) twoTvLayout.tvRight.textSize = tvRightSize
            if(tvLeftColor!=0) twoTvLayout.tvLeft.setTextColor(tvLeftColor)
            if(tvRightColor!=0) twoTvLayout.tvRight.setTextColor(tvRightColor)
        }
        attrArray?.recycle()
    }

    fun setLeftText(content:String) {
        twoTvLayout.tvLeft.text = content
    }

    fun setRightText(content: String) {
        twoTvLayout.tvRight.text = content
    }
}