package com.cali.libcore.util

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.NetworkInterface
import java.util.*

/**
 * createTime：2019/5/22/022 on 20:12
 *
 * @author: jyc dell
 */
object MacUtil {

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    private val macFromHardware: String
        get() {
            try {
                val all = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) {
                        continue
                    }

                    val macBytes = nif.hardwareAddress ?: return ""

                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }

                    if (res1.isNotEmpty()) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private val macAddress: String
        get() {
            return try {
                BufferedReader(FileReader(File("/sys/class/net/wlan0/address"))).readLine()
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    fun getMacAddress(context: Context): String {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getMacDefault(context)?:""
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            macAddress
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            macFromHardware
        }else {
            ""
        }
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context
     * @return
     */
    private fun getMacDefault(context: Context?): String? {
        var mac = "02:00:00:00:00:00"
        if (context == null) {
            return mac
        }

        val wifi = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        var info: WifiInfo? = null
        try {
            info = wifi.connectionInfo
        } catch (e: Exception) {
        }

        if (info == null) {
            return null
        }
        mac = info.macAddress
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH)
        }
        return mac
    }
}
