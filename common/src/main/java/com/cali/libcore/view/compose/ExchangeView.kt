package com.cali.libcore.view.compose

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.cali.common.R
import com.cali.libcore.util.MethodCheckLogin
import kotlinx.android.synthetic.main.exchange_view_dialog_layout.view.*
import kotlinx.android.synthetic.main.exchange_view_layout.view.image
import kotlinx.android.synthetic.main.exchange_view_layout.view.tv_one
import kotlinx.android.synthetic.main.exchange_view_layout.view.tv_three
import kotlinx.android.synthetic.main.exchange_view_layout.view.tv_two

class ExchangeView(context: Context, val attrs: AttributeSet?, def: Int) : LinearLayout(context, attrs, def) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var imageRes: Int = 0
    private var redPackageDialog: AlertDialog? = null

    init {
        orientation = VERTICAL
        gravity = CENTER_HORIZONTAL
        //加载视图
        val exchangeView = LayoutInflater.from(context)
            .inflate(R.layout.exchange_view_layout, this)
        val attrArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ExchangeView, 0, 0)
        attrArray?.apply {
            exchangeView.tv_one.text = attrArray.getString(R.styleable.ExchangeView_tv_one_text)
            exchangeView.tv_two.text = attrArray.getString(R.styleable.ExchangeView_tv_two_text)
            exchangeView.tv_three.text = attrArray.getString(R.styleable.ExchangeView_tv_three_text)
            imageRes = attrArray.getResourceId(R.styleable.ExchangeView_image_src, 0)
            exchangeView.image.setBackgroundResource(imageRes)
        }
        attrArray?.recycle()
    }


    fun showDialog(block:(dialog:AlertDialog?)->Unit) {

        val bigView = LayoutInflater.from(context)
            .inflate(R.layout.exchange_view_dialog_layout, null)
        bigView.tv_one.text = this@ExchangeView.tv_one.text
        bigView.tv_two.text = this@ExchangeView.tv_two.text
        bigView.tv_three.text = this@ExchangeView.tv_three.text
        bigView.image.setImageResource(imageRes)
        bigView.ivClose.setOnClickListener {
            redPackageDialog?.dismiss()
        }

        bigView.setOnClickListener {
            block(redPackageDialog)
        }

        redPackageDialog = AlertDialog.Builder(context as Activity)
            .setView(bigView)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
        redPackageDialog?.show()
    }

}