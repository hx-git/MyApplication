package com.cali.libcore.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.cali.common.R

/**
 * Created by Dinosa on 2018/1/30.
 */

class RedPacketDialog {

    private var alertDialog: AlertDialog? = null
    @JvmOverloads
    fun show(context: Context, goldNumber: String, mListener: DialogInterface.OnDismissListener? = null) {
        //红包的对话框
        val builder = AlertDialog.Builder(context, R.style.NoBackGroundDialog)
        val view = View.inflate(context, R.layout.dialog_red_packet, null)
        val titleView = view.findViewById<View>(R.id.tv_dialog_title) as TextView

        val title =
            "<big>恭喜您</big><br/>获得 <big><font color='" + context.resources.getColor(R.color.dialog_red_pack_number_text_color) + "'>" + goldNumber + "</font></big> 金币"
        titleView.text = Html.fromHtml(title)

        val bannerView = view.findViewById<View>(R.id.ll_ad) as ImageView
        bannerView.setOnClickListener { }
        view.findViewById<View>(R.id.tv_sure).setOnClickListener {
            if (alertDialog != null) {
                alertDialog!!.dismiss()
                //这里是点击了确定的操作；
            }
        }

        builder.setView(view)
        alertDialog = builder.create()
        if (mListener != null) {
            alertDialog!!.setOnDismissListener { dialog -> mListener.onDismiss(dialog) }
        }

        alertDialog!!.show()
    }

}
