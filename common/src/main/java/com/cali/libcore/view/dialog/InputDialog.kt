package com.cali.libcore.view.dialog

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.cali.common.R
import kotlinx.android.synthetic.main.dialog_input_layout.view.*

/**
 * Created by Dinosa on 2018/2/8.
 * 这里是一个输入内容的一个对话框
 */
class InputDialog {
    private lateinit var mAlertDialog:MaterialDialog
    fun show(context: Context, title: String,hint:String,block:(content:String) ->Unit) {
        val builder = MaterialDialog.Builder(context)
        //初始化自定义布局；
        val view = View.inflate(context, R.layout.dialog_input_layout, null)


        view.et_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    view.iv_close.visibility = View.INVISIBLE
                } else {
                    view.iv_close.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        view.tv_inputTitle.text = title
        //设置一些参数；
        builder.customView(view,false)
        builder.canceledOnTouchOutside(false)
        view.tv_sure.setOnClickListener {
            block(view.et_input.text.toString().trim())
        }

        view.tv_cancel.setOnClickListener {
            block(view.et_input.text.toString().trim())
        }

        mAlertDialog = builder.build()

        mAlertDialog.show()
    }

}
