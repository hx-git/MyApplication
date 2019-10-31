package com.cali.libcore.view.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cali.common.kt.debugLogInfo
import com.cali.common.R
import com.cali.libcore.view.behavior.source.ViewOffsetBehavior

import java.lang.ref.WeakReference

class MainHeaderBehavior : ViewOffsetBehavior<View> {

    private var mCurState = STATE_OPENED
    private var mHeaderStateListener: OnHeaderStateListener? = null

    private val mOverScroller: OverScroller by lazy {
        OverScroller(mContext)
    }

    private var mParent: WeakReference<CoordinatorLayout>? = null//CoordinatorLayout
    private var mChild: WeakReference<View>? = null//CoordinatorLayout的子View，即header

    //界面整体向上滑动，达到列表可滑动的临界点
    private var upReach: Boolean = false
    //列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private var downReach: Boolean = false
    //列表上一个全部可见的item位置
    private var lastPosition = -1

    private var mFlingRunnable: FlingRunnable? = null

    private lateinit var mContext: Context

    //tab上移结束后是否悬浮在固定位置
    private var tabSuspension = false

    private val headerOffset: Int
        get() = mContext.resources.getDimensionPixelOffset(R.dimen.header_offset)

    private val isClosed: Boolean
        get() = mCurState == STATE_CLOSED

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    override fun layoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.layoutChild(parent, child, layoutDirection)
        mParent = WeakReference(parent)
        mChild = WeakReference(child)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View,
        target: View, nestedScrollAxes: Int, type: Int
    ): Boolean {
        return if (tabSuspension) {
            nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0 && !isClosed
        } else nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }


    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        lastPosition = -1
        return !isClosed
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downReach = false
                upReach = false
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(child)
                "child:$child".debugLogInfo()
            }

        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }


    /**
     * @param coordinatorLayout
     * @param child             代表header
     * @param target            代表RecyclerView
     * @param dx
     * @param dy                上滑 dy>0， 下滑dy<0
     * @param consumed
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        //制造滑动视察，使header的移动比手指滑动慢
        val scrollY = dy / 4.0f

        when (target) {
            is NestedLinearLayout -> {//处理header滑动
                var finalY = child.translationY - scrollY
                if (finalY < headerOffset) {
                    finalY = headerOffset.toFloat()
                } else if (finalY > 0) {
                    finalY = 0f
                }
                child.translationY = finalY
                consumed[1] = dy
            }
            is RecyclerView -> {//处理列表滑动
                val pos = (target.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                //header closed状态下，列表上滑再下滑到第一个item全部显示，此时不让CoordinatorLayout整体下滑，
                //手指重新抬起再下滑才可以整体滑动
                if (pos == 0 && pos < lastPosition) {
                    downReach = true
                }

                if (pos == 0 && canScroll(child, scrollY)) {
                    //如果列表第一个item全部可见、或者header已展开，则让CoordinatorLayout消费掉事件
                    var finalY = child.translationY - scrollY
                    //header已经closed，整体不能继续上滑，手指抬起重新上滑列表开始滚动
                    if (finalY < headerOffset) {
                        finalY = headerOffset.toFloat()
                        upReach = true
                    } else if (finalY > 0) {//header已经opened，整体不能继续下滑
                        finalY = 0f
                    }
                    child.translationY = finalY
                    consumed[1] = dy//让CoordinatorLayout消费掉事件，实现整体滑动
                }
                lastPosition = pos
            }
            is NestedScrollView -> {
                val nsv = target
                var finalY = child.translationY - scrollY
                if (finalY < headerOffset) {
                    finalY = headerOffset.toFloat()
                } else if (finalY > 0) {
                    finalY = 0f
                }
                child.translationY = finalY
                consumed[1] = dy
                if (type == ViewCompat.TYPE_NON_TOUCH) {
                    handleActionUp(child)
                }
            }
        }
    }

    /**
     * 是否可以整体滑动
     *
     * @return
     */
    private fun canScroll(child: View, scrollY: Float): Boolean {
        if (scrollY > 0 && child.translationY > headerOffset) {
            return true
        }

        if (child.translationY == headerOffset.toFloat() && upReach) {
            return true
        }

        return scrollY < 0 && !downReach

    }

    private fun handleActionUp(child: View) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable)
            mFlingRunnable = null
        }
        //手指抬起时，header上滑距离超过总距离三分之一，则整体自动上滑到关闭状态
        if (child.translationY < headerOffset / 3.0f) {
            scrollToClose(DURATION_SHORT)
        } else {
            scrollToOpen(DURATION_SHORT)
        }
    }

    private fun onFlingFinished(layout: View) {
        changeState(if (isClosed(layout)) STATE_CLOSED else STATE_OPENED)
    }

    /**
     * 直接展开
     */
    fun openHeader() {
        openHeader(DURATION_LONG)
    }

    private fun openHeader(duration: Int) {
        if (isClosed) {
            mChild?.get()?.apply {
                this.removeCallbacks(mFlingRunnable)
                mFlingRunnable = null
                scrollToOpen(duration)
            }
        }
    }

    fun closeHeader() {
        closeHeader(DURATION_LONG)
    }

    private fun closeHeader(duration: Int) {
        if (!isClosed) {
            mChild?.get()?.apply {
                this.removeCallbacks(mFlingRunnable)
                mFlingRunnable = null
                scrollToClose(duration)
            }
        }
    }

    private fun isClosed(child: View): Boolean {
        return child.translationY == headerOffset.toFloat()
    }

    private fun changeState(newState: Int) {

        if (mCurState != newState) {
            mCurState = newState

            if (mHeaderStateListener == null) {
                return
            }

            if (mCurState == STATE_OPENED) {
                mHeaderStateListener?.onHeaderOpened()
            } else {
                mHeaderStateListener?.onHeaderClosed()
            }
        }
    }

    private fun scrollToClose(duration: Int) {
        val curTranslationY = mChild?.get()!!.translationY.toInt()
        val dy = headerOffset - curTranslationY
        mOverScroller.startScroll(0, curTranslationY, 0, dy, duration)
        start()
    }

    private fun scrollToOpen(duration: Int) {
        val curTranslationY = mChild?.get()!!.translationY
        mOverScroller.startScroll(0, curTranslationY.toInt(), 0, (-curTranslationY).toInt(), duration)
        start()
    }

    private fun start() {
        if (mOverScroller.computeScrollOffset()) {
            mFlingRunnable = FlingRunnable(mParent?.get()!!, mChild?.get())
            ViewCompat.postOnAnimation(mChild?.get()!!, mFlingRunnable)
        } else {
            onFlingFinished(mChild?.get()!!)
        }
    }

    private inner class FlingRunnable internal constructor(
        private val mParent: CoordinatorLayout,
        private val mLayout: View?
    ) : Runnable {

        override fun run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    mLayout.translationY = mOverScroller.currY.toFloat()
                    ViewCompat.postOnAnimation(mLayout, this)
                } else {
                    onFlingFinished(mLayout)
                }
            }
        }
    }

    fun setTabSuspension(tabSuspension: Boolean) {
        this.tabSuspension = tabSuspension
    }

    fun setHeaderStateListener(headerStateListener: OnHeaderStateListener) {
        mHeaderStateListener = headerStateListener
    }

    interface OnHeaderStateListener {
        fun onHeaderClosed()

        fun onHeaderOpened()
    }

    companion object {
        private val STATE_OPENED = 0
        private val STATE_CLOSED = 1
        private val DURATION_SHORT = 300
        private val DURATION_LONG = 600
    }

}