package com.cali.libcore.util

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Dinosa on 2018/2/2.
 * 格式化的一个帮助类；
 */

object FormatUtils {

    /**
     * 这里最多只能容纳24小时超过的话就会有问题；
     * 格式化时间返回，1000*10 返回为 00：10；
     * 1000*60*60+1000*60*5   01:05:00;
     * @param
     * @return 返回时间对应的字符串；
     */
    fun formatMillisToTime(date: Long): String {
        //这里的话， 我们对毫秒进行一个格式化的操作；
        var hours: Long = 0
        val seconds = date / 1000
        var minutes = seconds / 60
        val second = seconds % 60

        if (minutes >= 60) {
            hours = minutes / 60
            minutes %= 60
        }
        var time = ""
        if (hours == 0L) {
            //这里表示没有小时；
            time += formatNumberToString(minutes)
            time += ":" + formatNumberToString(second)

        } else {
            //这里表示有小时的字符串拼接；
            time += formatNumberToString(hours)
            time += ":" + formatNumberToString(minutes)
            time += ":" + formatNumberToString(second)
        }
        return time
    }

    /**
     * 这里最多只能容纳24小时超过的话就会有问题；
     * 格式化时间返回，1000*10 返回为 00：10；
     * 1000*60*60+1000*60*5   01:05:00;
     * @param
     * @return 返回时间对应的字符串；
     */
    fun formatSecondToTime(date: Long): String {
        //这里的话， 我们对毫秒进行一个格式化的操作；
        var hours: Long = 0
        var minutes = date / 60
        val second = date % 60

        if (minutes >= 60) {
            hours = minutes / 60
            minutes %= 60
        }
        var time = ""
        if (hours == 0L) {
            //这里表示没有小时；
            time += formatNumberToString(minutes)
            time += ":" + formatNumberToString(second)

        } else {
            //这里表示有小时的字符串拼接；
            time += formatNumberToString(hours)
            time += ":" + formatNumberToString(minutes)
            time += ":" + formatNumberToString(second)
        }
        return time
    }


    /**
     * 这里我们是对大于0的number进行一个格式化；
     * @param number 需要格式化的数字
     * @return 小于10的，在前面+0，否则原样子返回；
     */
    private fun formatNumberToString(number: Long): String {
        var time = ""
        if (number < 10) {
            time += "0$number"
        } else {
            time += number
        }
        return time
    }

    /**
     * 格式化日期
     * @param longTime 整形的时间；
     * @param pattern yyyy-MM-dd
     * @return
     */
    private fun formatDate(longTime: Long, pattern: String): String {
        return SimpleDateFormat(pattern).format(Date(longTime))
    }

    /**
     * 默认的格式化日期；
     * @param longTime
     * @return
     */
    fun defaultFormatDateToString(longTime: Long): String {
        return formatDate(longTime, "yyyy-MM-dd")
    }


    /**
     * 默认的格式化日期；
     * @param date
     * @return
     */
    fun defaultFormatStringToDate(date: String): Date? {
        return formatStringToDate(date, "yyyy-MM-dd")
    }

    /**
     * 这里是将字符串转换为date数据；
     * @return
     */
    private fun formatStringToDate(str: String, pattern: String): Date? {

        try {
            return SimpleDateFormat(pattern).parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 保留1位小数
     * @param countPrice
     * @return
     */
    fun decimalFormat(countPrice: Float): String {
        val df = DecimalFormat("#.0")
        return formatDotString(df.format(countPrice.toDouble()))
    }

    /**
     * 保留2位小数
     * @param countPrice
     * @return
     */
    fun decimalFormatTwo(countPrice: Float): String {
        val df = DecimalFormat("#.00")
        val format = df.format(countPrice.toDouble())
        //上面有一个问题就是如果是0.4会变成.4
        return formatDotString(format)
    }

    /**
     * 将.4 转换为0.4；
     * @param targetStr
     * @return
     */
    private fun formatDotString(targetStr: String): String {
        var targetStr = targetStr
        val substring = targetStr.substring(0, 1)
        if ("." == substring) {
            targetStr = "0$targetStr"
        }
        return targetStr
    }


    /**
     * 使用md5的算法进行加密
     */
    fun md5(plainText: String): String {
        var secretBytes: ByteArray? = null
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                plainText.toByteArray()
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("没有md5这个算法！")
        }

        var md5code = BigInteger(1, secretBytes).toString(16)// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }
}
