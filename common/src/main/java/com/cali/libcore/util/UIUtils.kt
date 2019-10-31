package com.cali.libcore.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

/**
 * Created by Dinosa on 2018/1/22.
 */

object UIUtils {

    private var sToast: Toast? = null

    private val MIN_CLICK_DELAY_TIME = 3000
    private var lastClickTime: Long = 0

    val isFastClick: Boolean
        get() {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime
                flag = true
            }
            return flag
        }

    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= 19) {
            getStatusHeight(context)
        } else {
            0
        }
    }

    /**
     * 提现按钮限制点击
     *
     * @return
     */
    val isFastTXClick: Boolean
        get() {
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                flag = true
            }
            lastClickTime = curClickTime
            return flag
        }

    fun toast(context: Context, text: String) {

        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        }
        sToast?.setText(text)
        sToast?.show()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    private fun getStatusHeight(context: Context): Int {

        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(
                clazz.getField("status_bar_height")
                    .get(`object`).toString()
            )
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 这里是将sp转换为px
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {

        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,

            spValue, context.resources.displayMetrics
        ).toInt()
    }


    /**
     * @param mContext Context
     * @param time     倒计时的时间
     * @param btn      倒计时的控件
     */
    fun countDowm(
        mContext: Context, time: Int,
        btn: TextView, tvText: String
    ) {

        val timer = object : CountDownTimer((time * 600).toLong(), 1000) {
            @SuppressLint("NewApi")
            override fun onTick(millisUntilFinished: Long) {
                btn.text = "还剩" + millisUntilFinished / 600 + "秒"
                btn.isClickable = false
            }

            @SuppressLint("NewApi")
            override fun onFinish() {
                btn.text = tvText
                btn.isClickable = true
            }
        }
        timer.start()
    }

    /**
     * 获取屏幕的宽度；
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context?): Int {
        if (context == null) {
            return 0
        }
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }


    /**
     * 获取屏幕的高度；
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context?): Int {
        if (context == null) {
            return 0
        }
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }


    fun getSmallWidth(activity: Activity): Float {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val heightPix = dm.heightPixels
        val widthPix = dm.widthPixels
        val heightDp = heightPix/dm.density
        val widthDp = widthPix/dm.density
        return if (widthDp < heightDp) {
            widthDp
        }else{
            heightDp
        }
    }

}
