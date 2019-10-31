package com.cali.libcore.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.cali.common.R

class BottomDialog(context: Context,style: Int):Dialog(context,style) {
    private var isBackCancelable = false
    private var view: View = FrameLayout(context)
    constructor(context: Context) : this(context, R.style.BottomDialogStyle)

    constructor(builder: Builder) : this(builder.context){
        this.view = builder.view
        this.isBackCancelable = builder.isBackCancelable
        setCancelable(builder.isCancelable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        setCanceledOnTouchOutside(isBackCancelable)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        }

    }

    class Builder(val context: Context){

        var isBackCancelable = false
        var view: View = FrameLayout(context)
        var isCancelable = false

        fun isBackCancelable(cancelable:Boolean):Builder{
            isBackCancelable = cancelable
            return this
        }

        fun view(view: View): Builder {
            this.view = view
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            this.isCancelable = cancelable
            return this
        }

        fun build(): BottomDialog {
            return BottomDialog(this)
        }
    }
}