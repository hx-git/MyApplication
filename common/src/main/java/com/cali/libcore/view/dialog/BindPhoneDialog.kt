package com.cali.libcore.view.dialog

import android.content.Context
import android.view.LayoutInflater
import com.afollestad.materialdialogs.MaterialDialog
import com.cali.common.kt.countDown
import com.cali.common.kt.toast
import com.cali.common.R
import com.cali.libcore.kt.TYPE_BIND
import com.cali.libcore.kt.TYPE_CODE
import com.cali.libcore.util.UIUtils
import kotlinx.android.synthetic.main.dialog_bind_phone_number.view.*

class BindPhoneDialog {

    private var phoneNum:String = ""
    private var phoneCode:String = ""
    private val layoutId:Int = R.layout.dialog_bind_phone_number
    fun show(context: Context,block:(type:Int,content:String)->Unit) {
        val view = LayoutInflater.from(context)
            .inflate(layoutId,null)
        MaterialDialog.Builder(context)
            .customView(view,false)
            .title("绑定手机号")
            .negativeText("取消")
            .positiveText("确定")
            .canceledOnTouchOutside(false)
            .onPositive { dialog, which ->
                if (view.et_phone_number_code.text.toString().trim().also {
                        phoneCode = it
                    }.isEmpty()) {
                    context.toast("验证码不能为空")
                    return@onPositive
                }
                if (view.et_phone.text.toString().trim().isEmpty()) {
                    context.toast("手机号不能为空")
                    return@onPositive
                }
                if (view.et_phone.text.toString().trim() != phoneNum) {
                    context.toast("手机号不一致")
                    return@onPositive
                }
                block(TYPE_BIND,phoneCode)
            }
            .onNegative{ dialog, which ->
                dialog.dismiss()
            }
            .build()
            .show()

        view.tv_send_proof.setOnClickListener {
            if (view.et_phone.text.toString().trim().also { phoneNum = it }.isEmpty()) {
                context.toast("手机号不能为空")
                return@setOnClickListener
            }
            //倒计时
            view.tv_send_proof.countDown(60,"获取")
            block(TYPE_CODE,phoneNum)
        }
    }
}