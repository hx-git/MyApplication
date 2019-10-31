package com.cali.libcore.util.rsa

import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * 签名验签操作；
 * @author Dinosa
 */
object RSASignature {


    /**
     * 签名算法
     */
    private val SIGN_ALGORITHMS = "SHA1WithRSA"

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param encode 字符集编码
     * @return 签名值
     */
    fun sign(content: String, privateKey: String, encode: String): String? {
        try {
            val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey))

            val keyf = KeyFactory.getInstance("RSA")
            val priKey = keyf.generatePrivate(priPKCS8)

            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)

            signature.initSign(priKey)
            signature.update(content.toByteArray(charset(encode)))

            val signed = signature.sign()

            return Base64.encode(signed)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun sign(content: String, privateKey: String): String? {
        try {
            val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey))
            val keyf = KeyFactory.getInstance("RSA")
            val priKey = keyf.generatePrivate(priPKCS8)
            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initSign(priKey)
            signature.update(content.toByteArray())
            val signed = signature.sign()
            return Base64.encode(signed)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @param encode 字符集编码
     * @return 布尔值
     */
    fun doCheck(content: String, sign: String, publicKey: String, encode: String): Boolean {
        try {
            val keyFactory = KeyFactory.getInstance("RSA")
            val encodedKey = Base64.decode(publicKey)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))


            val signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS)

            signature.initVerify(pubKey)
            signature.update(content.toByteArray(charset(encode)))

            return signature.verify(Base64.decode(sign))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun doCheck(content: String, sign: String, publicKey: String): Boolean {
        try {
            val keyFactory = KeyFactory.getInstance("RSA")
            val encodedKey = Base64.decode(publicKey)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))


            val signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS)

            signature.initVerify(pubKey)
            signature.update(content.toByteArray())

            return signature.verify(Base64.decode(sign))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

}  

