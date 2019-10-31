package com.cali.libcore.util.rsa

import java.security.MessageDigest
import kotlin.experimental.and

object Md5Utils {

    private var md5: MessageDigest? = null

    init {
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            println(e.message)
        }

    }

    /**
     * 用于获取一个String的md5值
     * @return
     */
    fun getMd5(str: String): String {
        val bs = md5!!.digest(str.toByteArray())
        val sb = StringBuilder(40)
        for (x in bs) {
            if ((x and 0xff.toByte()).toInt() shr 4 == 0) {
                sb.append("0").append(Integer.toHexString((x and 0xff.toByte()).toInt()))
            } else {
                sb.append(Integer.toHexString((x and 0xff.toByte()).toInt()))
            }
        }
        return sb.toString()
    }

}
