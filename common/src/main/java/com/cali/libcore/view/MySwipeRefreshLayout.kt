package com.cali.libcore.view

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MySwipeRefreshLayout(context: Context,attrs: AttributeSet?)
    :SwipeRefreshLayout(context,attrs) {
    constructor(context: Context):this(context,null)

    private var mMeasured = false
    private var mPreMeasureRefreshing = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!mMeasured) {
            mMeasured = true
            isRefreshing = mPreMeasureRefreshing
        }
    }

    override fun setRefreshing(refreshing: Boolean) {
        super.setRefreshing(refreshing)
        if (mMeasured) {
            super.setRefreshing(refreshing)
        }else{
            mPreMeasureRefreshing = refreshing
        }
    }
}