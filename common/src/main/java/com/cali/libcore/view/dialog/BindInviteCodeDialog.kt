package com.cali.libcore.view.dialog

import android.content.Context
import android.view.LayoutInflater
import com.afollestad.materialdialogs.MaterialDialog
import com.cali.common.R
import com.cali.common.kt.gone
import com.cali.common.kt.toast
import com.cali.common.kt.visible
import kotlinx.android.synthetic.main.dialog_bind_invite_code.view.*

class BindInviteCodeDialog {
    fun show(context: Context,code:String?,block:(content:String)->Boolean) {
        val inviteView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_bind_invite_code,null)
        val builder = MaterialDialog.Builder(context)
            .customView(inviteView,false)

        if (code == null || code.trim().isEmpty()) {
            builder.title("填写邀请码")
            builder.negativeText("取消")
            builder.positiveText("确定")
            builder.onPositive { dialog, which ->
                val content = inviteView.etInvite.text.toString().trim()
                if (content.isEmpty()) {
                    context.toast("邀请码不能为空")
                    return@onPositive
                }
                block(content).apply {
                    if (this) {
                        dialog.dismiss()
                    }
                }
            }
        }else {
            inviteView.etInvite?.apply {
                gone()
                isFocusableInTouchMode = false
                isEnabled = false
                isFocusable = false
                keyListener = null
            }
            inviteView.tvInvite.apply {
                visible()
                text = code
            }
            builder.title("您的邀请码")
        }

        builder.show()
    }
}