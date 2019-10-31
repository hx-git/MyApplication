package com.cali.libcore.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.*
import androidx.annotation.LayoutRes
import com.cali.common.R
import com.cali.libcore.util.UIUtils

/**
 * Created by zd on 16/6/21.
 */
abstract class BaseDialog : Dialog {


    lateinit var view: View
        internal set
    var mContext: Context

    constructor(context: Context) : super(context, R.style.dialog) {
        this.mContext = context
    }

    constructor(context: Context, fromButton: Boolean) : super(context, R.style.Dialog_NoTitle) {
        if (fromButton) {
            fromBottom()
        }
        this.mContext = context
    }


    override fun setContentView(@LayoutRes resource: Int) {
        view = LayoutInflater.from(mContext).inflate(resource, null)
        //设置SelectPicPopupWindow的View
        setContentView(view)
        //设置全屏
        val w = (0.8 * UIUtils.getScreenWidth(mContext)).toInt()
        val h = (0.6 * UIUtils.getScreenHeight(mContext)).toInt()
        window?.setLayout(w, h)
        // ViewGroup.LayoutParams.WRAP_CONTENT
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private fun fromBottom() {
        val localLayoutParams = window.attributes
        localLayoutParams.gravity = Gravity.BOTTOM or Gravity.LEFT
        localLayoutParams.width = UIUtils.getScreenWidth(context)
        localLayoutParams.y = 0
        localLayoutParams.x = 0
        onWindowAttributesChanged(localLayoutParams)
    }
}
