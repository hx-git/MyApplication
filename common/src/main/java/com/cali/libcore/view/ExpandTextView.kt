package com.cali.libcore.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import com.cali.common.R


class ExpandTextView : FrameLayout {
    private lateinit var contentText: TextView
    private lateinit var textPlus: TextView
    private var showLines: Int = 0
    private var expandStatusListener: ExpandStatusListener? = null
    private var expandClickListener: ExpandTextViewClickListener? = null
    var isExpand: Boolean = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0)
        try {
            showLines = typedArray.getInt(R.styleable.ExpandTextView_showLines, DEFAULT_MAX_LINES)
        } finally {
            typedArray.recycle()
        }
    }

    private fun initView() {
        //        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_magic_text, this)
        contentText = findViewById(R.id.contentText)
        if (showLines > 0) {
            contentText.maxLines = showLines
        }

        textPlus = findViewById(R.id.textPlus)
        textPlus.setOnClickListener {
            //判断是否有外部设置的点击事件
            if (expandClickListener != null) {
                expandClickListener?.onClick()
            }else{
                val textStr = textPlus.text.toString().trim { it <= ' ' }
                if (FULL_TEXT_TAG == textStr) {
                    contentText.maxLines = Integer.MAX_VALUE
                    textPlus.text = CLOSE_TEXT_TAG
                    isExpand = true
                } else {
                    contentText.maxLines = showLines
                    textPlus.text = FULL_TEXT_TAG
                    isExpand = false
                }
                //通知外部状态已变更
                expandStatusListener?.statusChange(isExpand)
            }
        }

        this.setOnClickListener {
            expandClickListener?.onClick()
        }
    }

    fun getText(): String {
        return contentText.text.toString()
    }

    fun setText(content: CharSequence) {

        //在开始绘制contentText内容时，进行监听
        contentText.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // 避免重复监听
                contentText.viewTreeObserver.removeOnPreDrawListener(this)
                //获取当前文本的行数
                val linCount = contentText.lineCount
                if (linCount > showLines) {
                    if (isExpand) {
                        contentText.maxLines = Integer.MAX_VALUE
                        textPlus.text = CLOSE_TEXT_TAG
                    } else {
                        contentText.maxLines = showLines
                        textPlus.text = FULL_TEXT_TAG
                    }
                    textPlus.visibility = View.VISIBLE
                } else {
                    textPlus.visibility = View.GONE
                }
                return true
            }
        })
        contentText.text = content
    }

    fun setExpandStatusListener(listener: ExpandStatusListener) {
        this.expandStatusListener = listener
    }

    fun setOnExpandTextViewClickListener(listener: ExpandTextViewClickListener) {
        this.expandClickListener = listener
    }

    interface ExpandTextViewClickListener {
        fun onClick()
    }

    interface ExpandStatusListener {
        fun statusChange(isExpand: Boolean)
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
////        return textPlus.dispatchTouchEvent(event) //这里textPlus会拦截整个布局的点击事件
//        return super.dispatchTouchEvent(event)
//    }

    companion object {
        const val DEFAULT_MAX_LINES = 3
        const val FULL_TEXT_TAG = "...展开"
        const val CLOSE_TEXT_TAG = "收起"
    }

}
