package com.cali.libcore.view.compose

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.cali.common.R
import kotlinx.android.synthetic.main.include_vertical_two_tv.view.*

class VerticalTwoTextView(context: Context, val attrs: AttributeSet?, def: Int) : LinearLayout(context, attrs, def) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    private var twoTvLayout:View
    init {
        orientation = VERTICAL
        gravity = CENTER_HORIZONTAL
        //加载视图
        twoTvLayout = LayoutInflater.from(context)
            .inflate(R.layout.include_vertical_two_tv, this)
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.VerticalTwoTextView, 0, 0)
        attrArray?.apply {
            twoTvLayout.tvTop.text = attrArray.getString(R.styleable.VerticalTwoTextView_tv_top_text)
            twoTvLayout.tvBottom.text = attrArray.getString(R.styleable.VerticalTwoTextView_tv_bottom_text)
            val tvTopColor = attrArray.getColor(R.styleable.VerticalTwoTextView_tv_top_text_color, 0)
            val tvBottomColor = attrArray.getColor(R.styleable.VerticalTwoTextView_tv_bottom_text_color, 0)
            val tvTopSize = attrArray.getDimension(R.styleable.VerticalTwoTextView_tv_top_text_size,0f)
            val tvBottomSize = attrArray.getDimension(R.styleable.VerticalTwoTextView_tv_bottom_text_size,0f)
            if(tvTopSize!=0f) twoTvLayout.tvTop.textSize = tvTopSize
            if(tvBottomSize!=0f) twoTvLayout.tvBottom.textSize = tvBottomSize
            if(tvTopColor!=0) twoTvLayout.tvTop.setTextColor(tvTopColor)
            if(tvBottomColor!=0) twoTvLayout.tvBottom.setTextColor(tvBottomColor)
        }
        attrArray?.recycle()
    }

    fun setTopText(content:String) {
        twoTvLayout.tvTop.text = content
    }

    fun setBottomText(content: String) {
        twoTvLayout.tvBottom.text = content
    }
}