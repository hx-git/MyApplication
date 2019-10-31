package com.cali.libcore.util

import net.arvin.socialhelper.SocialHelper

/**
 * Created by arvinljw on 17/11/27 17:33
 * Function：
 * Desc：
 */
 object SocialUtil{
    //qq app key MEeumycxgpdrZHJx
    //wb app secret e567fcd20e0b863901bf01ae4f570eab
    var socialHelper :SocialHelper = SocialHelper.Builder()
        .setQqAppId("1109767740")
        .setWxAppId("wx5b85a3382dc34a6f")
        .setWxAppSecret("c350e672e3ead9fe1ee188a1e2dd68ba")
        .setNeedLoinResult(true)
        .setWbAppId("2515040330")
        .setWbRedirectUrl("wbRedirectUrl")
        .build()
}
