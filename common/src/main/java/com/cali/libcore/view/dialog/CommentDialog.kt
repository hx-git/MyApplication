package com.cali.libcore.view.dialog

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.cali.common.R
import com.cali.libcore.util.UIUtils

/**
 * Created by Dinosa on 2018/2/2.
 * 评论的对话框；
 */

class CommentDialog {

    private var mAlertDialog: AlertDialog? = null

    fun show(context:Context, mOnClickListener: OnDialogClickListener?) {

        //红包的对话框
        val builder = AlertDialog.Builder(context, R.style.inputDialog)
        val view = View.inflate(context, R.layout.dialog_comment_dialog, null)

        val viewGroup = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = viewGroup

        val commentContent = view.findViewById<View>(R.id.et_comment) as EditText
        view.findViewById<View>(R.id.tv_send).setOnClickListener(View.OnClickListener {
            val trim = commentContent.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(trim)) {
                UIUtils.toast(context, "内容不能为空!")
                return@OnClickListener
            }
            mOnClickListener?.onClickSure(trim)
            mAlertDialog!!.dismiss()
        })
        view.findViewById<View>(R.id.tv_cancel).setOnClickListener { mAlertDialog!!.dismiss() }
        builder.setView(view)
        mAlertDialog = builder.create()

        mAlertDialog!!.setOnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.repeatCount == 0)
                mAlertDialog!!.cancel()
            false
        }
        mAlertDialog!!.show()

        val layoutParams = mAlertDialog!!.window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM or Gravity.LEFT
        layoutParams.width = context.resources.displayMetrics.widthPixels

        mAlertDialog!!.window!!.attributes = layoutParams
    }


    interface OnDialogClickListener {
        fun onClickSure(Content: String)
    }
}
