package com.cali.libcore.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ContentFrameLayout
import androidx.appcompat.widget.Toolbar
import com.cali.common.R
import com.cali.common.kt.debugLogInfo
import com.cali.common.kt.gone
import com.cali.common.kt.visible
import com.cali.libcore.util.UIUtils
import kotlinx.android.synthetic.main.toolbar_layout.view.*


/**
 * 如果自定义toolbar 高度
 * 必须将 WRAP_CONTENT 改为固定值
 */
class ToolbarHelper(private val mContext: Context, view: Any) {
    /**
     * base setView
     */
    var contentView: ContentFrameLayout? = null
        private set
    /**
     * 用户定义的视图
     */
    private var mUserView: View? = null
    /**
     * 视图构造器
     */
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private val statusBarSize by lazy {
        if (Build.VERSION.SDK_INT >= 19) {
            UIUtils.dip2px(mContext,25f)
        } else {
            0
        }
    }

    init {
        mUserView = when (view) {
            is Int -> mInflater.inflate(view, null)
            is View -> view
            else -> throw ClassCastException("ToolBarHelper setLayout() type must be int or View")
        }
        //初始化新布局
        initContentView()
        //添加用户布局 margin = statusBar + toolbar
        intiUserView(getToolbarHeight())
        //添加boolBar margin = statusBar
        initToolBar()
    }

    @SuppressLint("RestrictedApi")
    private fun initContentView() {
        //直接创建一个帧布局，作为视图容器的父容器
        contentView = ContentFrameLayout(mContext)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        contentView?.layoutParams = params
    }

    private fun intiUserView(toolbarSize:Int) {
        contentView?.removeView(mUserView)
        //设置自定义属性到控件中
        val typedArray = mContext.theme.obtainStyledAttributes(ATTRS)
        //主题中定义的悬浮标志
        val overly = typedArray.getBoolean(0, false)
        //获取主题中定义的toolBar的高度
        //168
//        val toolBarSize = typedArray.getDimension(
//            1,
//            mContext.resources.getDimension(R.dimen.dp_32)
//        ).toInt()
        typedArray.recycle()
        //如果是悬浮状态，则不需要设置间距 再加statusbar的高度
        "$toolbarSize - $statusBarSize".debugLogInfo()
        params.topMargin = if (overly) statusBarSize  else toolbarSize + statusBarSize
        contentView?.addView(mUserView, params)
    }

    lateinit var toolbar: Toolbar

    private fun initToolBar() {
        /*
         * 通过inflater获取toolBar的布局文件
         */
        toolbar = mInflater.inflate(R.layout.toolbar_layout,null) as Toolbar
        val barParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,getToolbarHeight())
        barParams.topMargin = statusBarSize
        contentView?.addView(toolbar,barParams)
    }

    /**
     * 设置中间标题
     *
     * @param txt 文本
     */
    fun setTitleText(txt: String?) {
        toolbar.tvTitle.text = txt
    }

    /**
     * 设置右侧标题
     *
     * @param txt 文本
     */
    fun setRightText(txt: String) {
        toolbar.tvRight.text = txt
    }

    fun resetToolbarHeight() {
        intiUserView(getToolbarHeight())
    }

    private fun getToolbarHeight(): Int {
        return UIUtils.dip2px(mContext,40f)
    }

    fun setLeftText(txt:String,showTitle:Boolean = false) {
        toolbar.tvLeft?.apply {
            text = txt
            visible()
        }
        "zuoce:$txt".debugLogInfo()
        if (showTitle) {
            toolbar.tvTitle.visible()
        }else{
            toolbar.tvTitle.gone()
        }
    }

    fun titleClick(block: () -> Unit) {
        toolbar.tvTitle.setOnClickListener {
            block()
        }
    }

    fun rightClick(block: () -> Unit) {
        toolbar.tvRight.setOnClickListener {
            block()
        }
    }

    fun leftClick(block: () -> Unit) {
        toolbar.tvLeft.setOnClickListener {
            block()
        }
    }

    fun getLeftView(): AppCompatTextView? {
        return toolbar.tvLeft
    }


    companion object {
        /**
         * 两个属性
         * 1，toolbar是否悬浮在窗口之上
         * 2，toolbar的高度获取
         */
        private val ATTRS = intArrayOf(R.attr.windowActionBarOverlay, R.attr.actionBarSize)
    }
}
