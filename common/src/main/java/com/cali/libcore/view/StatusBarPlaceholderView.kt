package com.cali.libcore.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.cali.libcore.util.UIUtils

/**
 * 描述：状态栏占位视图
 * 日期：2019/4/26
 */
class StatusBarPlaceholderView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), UIUtils.getStatusBarHeight(context))
    }

}
