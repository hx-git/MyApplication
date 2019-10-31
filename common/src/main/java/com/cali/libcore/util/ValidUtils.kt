package com.cali.libcore.util

import android.text.TextUtils

import java.util.regex.Pattern

/**
 * Created by Dinosa on 2018/1/31.
 */

object ValidUtils {

    /**
     * @param mobiles
     * @return 验证手机号格式
     */
    fun isMobileNO(mobiles: String): Boolean {

        val telRegex = "[1][3456789]\\d{9}"
        return if (TextUtils.isEmpty(mobiles))
        //
            false
        else
            mobiles.matches(telRegex.toRegex())
    }

    fun isEmail(email: String): Boolean {
        val regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
        return Pattern.matches(regex, email)
    }
}
