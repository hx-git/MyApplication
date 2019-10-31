package com.cali.common.kt

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver


/**
 * 保存全局变量 容易引起内存泄露问题
 *
 */
fun Activity.keyBoardListener(block:(isHide:Boolean,height:Int,listener:ViewTreeObserver.OnGlobalLayoutListener)->Unit){
    //获取activity的根视图
    val rootView = this.window.decorView
    var rootViewVisibleHeight = 0//纪录根视图的显示高度
    val listener = object :ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            //获取当前根视图在屏幕上显示的大小
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val visibleHeight = rect.height()

            when (rootViewVisibleHeight - visibleHeight) {
                - visibleHeight -> {
                    rootViewVisibleHeight = visibleHeight
                }
                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                0 -> {}
                //根视图显示高度变小超过200，可以看作软键盘显示了
                in 200 until  1000 -> {
                    block(false,rootViewVisibleHeight - visibleHeight,this)
                    rootViewVisibleHeight = visibleHeight
                }
                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                in -1000 until -200 ->{
                    block(true,visibleHeight - rootViewVisibleHeight,this)
                    rootViewVisibleHeight = visibleHeight
                }
            }
        }
    }
    //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
    rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
}

/**
 * 传递引用，貌似没有内存泄露
 */
fun Activity.removeKeyBoardListener(listener: ViewTreeObserver.OnGlobalLayoutListener?) {
    window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
}
