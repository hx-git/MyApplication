package com.cali.libcore.base

import android.content.Context
import com.cali.libcore.util.SystemUtils
import com.cali.libcore.util.preference.StringPreference

/**
 * 描述：在主Module application中初始化
 */

class ContextHolder{
    companion object{
        lateinit var application:Context
        @JvmStatic
        var versionCode:String = SystemUtils.systemVersion
        @JvmStatic
        var channel:String = ""
        @JvmStatic
        var uid:String by StringPreference
        @JvmStatic
        var token:String by StringPreference

        @JvmStatic
        fun init(context: Context) {
            application = context
        }
    }
}
