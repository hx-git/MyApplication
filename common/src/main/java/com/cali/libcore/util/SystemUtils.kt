package com.cali.libcore.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*

/**
 * Created by Dinosa on 2018/1/24.
 * 获取系统的一些信息；
 */

object SystemUtils {


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    val systemLanguageList: Array<Locale>
        get() = Locale.getAvailableLocales()

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE


    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemApi: String
        get() = Build.VERSION.SDK_INT.toString() + ""

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val systemModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = Build.BRAND

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(ctx: Context): String {
        var imei = ""
        try {
            val tm = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
            imei =  tm.deviceId
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imei
    }

    /**
     * 向剪切板写入字符串
     */
    fun setClipString(context: Context, clip: String) {
        val myClipboard = context.getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager
        val myClip = ClipData.newPlainText("text", clip)
        myClipboard.primaryClip = myClip
    }


    /**
     * 这里获取uid
     * @param context
     * @return
     */
    fun getOpenUid(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 这获取分辨率；
     * @return
     */
    fun getResolution(context: Context): String {

        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels.toString() + "*" + displayMetrics.heightPixels
    }

    /**
     * 这里获取dpi
     * @param context
     * @return
     */
    fun getDensity(context: Context): String {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.densityDpi.toString() + ""
    }

    /**
     * 这里我们获取版本号；
     * @return
     */
    fun getVersionCode(context: Context): String {

        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val versionCode = packageInfo.versionCode
            return versionCode.toString() + ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return "0"
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    fun getAppIsDebug(ctx: Context?): Boolean? {
        val key = "DEBUG"
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null
        }
        var resultData: Boolean? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                val applicationInfo = packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getBoolean(key)

                    }
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return resultData
    }

}
