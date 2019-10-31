package com.cali.libcore.util

import com.cali.libcore.base.ContextHolder
import com.cali.libcore.util.rsa.Base64
import com.cali.libcore.util.rsa.RSAEncrypt

import java.nio.charset.Charset

/**
 * Created by Dinosa on 2018/3/7.
 */

object SecurityUtils {
    fun decodeRsaData(data: String?): String {

        try {
            val privateKeyInputStream = ContextHolder.application.assets.open("rsa/pkcs8_private_key.pem")

            // 私钥解密过程
            val res = RSAEncrypt.decrypt(
                RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privateKeyInputStream)),
                Base64.decode(data)
            )?:ByteArray(10)
            return String(res, Charset.forName("utf-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}
