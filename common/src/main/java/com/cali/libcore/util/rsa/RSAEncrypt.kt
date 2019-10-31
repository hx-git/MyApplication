package com.cali.libcore.util.rsa

import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import java.io.*
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import kotlin.experimental.and


object RSAEncrypt {
    /**
     * 字节数据转字符串专用集合
     */
    private val HEX_CHAR = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * 随机生成密钥对
     */
    fun genKeyPair(filePath: String) {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        var keyPairGen: KeyPairGenerator? = null
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA")
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen!!.initialize(1024, SecureRandom())
        // 生成一个密钥对，保存在keyPair中
        val keyPair = keyPairGen.generateKeyPair()
        // 得到私钥
        val privateKey = keyPair.private as RSAPrivateKey
        // 得到公钥
        val publicKey = keyPair.public as RSAPublicKey
        try {
            // 得到公钥字符串
            val publicKeyString = Base64.encode(publicKey.encoded)
            // 得到私钥字符串
            val privateKeyString = Base64.encode(privateKey.encoded)
            // 将密钥对写入到文件
            val pubfw = FileWriter("$filePath/publicKey.keystore")
            val prifw = FileWriter("$filePath/privateKey.keystore")
            val pubbw = BufferedWriter(pubfw)
            val pribw = BufferedWriter(prifw)
            pubbw.write(publicKeyString)
            pribw.write(privateKeyString)
            pubbw.flush()
            pubbw.close()
            pubfw.close()
            pribw.flush()
            pribw.close()
            prifw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 从文件中输入流中加载公钥
     *
     * 公钥输入流
     * @throws Exception
     * 加载公钥时产生的异常
     */
    @Throws(Exception::class)
    fun loadPublicKeyByFile(path: File): String {
        try {

            val br = BufferedReader(FileReader(path))
            var readLine: String? = null
            val sb = StringBuilder()
            while ( br.readLine().also { readLine = it } != null) {
                sb.append(readLine)
            }
            br.close()
            return sb.toString()
        } catch (e: IOException) {
            throw Exception("公钥数据流读取错误")
        } catch (e: NullPointerException) {
            throw Exception("公钥输入流为空")
        }

    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr
     * 公钥数据字符串
     * @throws Exception
     * 加载公钥时产生的异常
     */
    @Throws(Exception::class)
    fun loadPublicKeyByStr(publicKeyStr: String): RSAPublicKey {
        try {
            val buffer = Base64.decode(publicKeyStr)
            val keyFactory = KeyFactory.getInstance("RSA")
            val keySpec = X509EncodedKeySpec(buffer)
            return keyFactory.generatePublic(keySpec) as RSAPublicKey
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此算法")
        } catch (e: InvalidKeySpecException) {
            throw Exception("公钥非法")
        } catch (e: NullPointerException) {
            throw Exception("公钥数据为空")
        }

    }

    /**
     * 从文件中加载私钥
     *
     * 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPrivateKeyByFile(path: File): String {
        try {
            val br = BufferedReader(FileReader(path))
            var readLine: String? = null
            val sb = StringBuilder()
            while (br.readLine().also { readLine = it } != null) {
                sb.append(readLine)
            }
            br.close()
            return sb.toString()
        } catch (e: IOException) {
            throw Exception("私钥数据读取错误")
        } catch (e: NullPointerException) {
            throw Exception("私钥输入流为空")
        }

    }

    /**
     * 从文件中加载私钥
     *
     * 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPrivateKeyByFile(path: InputStream): String {
        try {

            val br = BufferedReader(InputStreamReader(path))
            var readLine: String? = null
            val sb = StringBuilder()
            while (br.readLine().also { readLine = it }!= null) {
                sb.append(readLine)
            }
            br.close()
            return sb.toString()
        } catch (e: IOException) {
            throw Exception("私钥数据读取错误")
        } catch (e: NullPointerException) {
            throw Exception("私钥输入流为空")
        }

    }

    @Throws(Exception::class)
    fun loadPrivateKeyByStr(privateKeyStr: String): RSAPrivateKey {
        try {
            val buffer = Base64.decode(privateKeyStr)
            val keySpec = PKCS8EncodedKeySpec(buffer)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此算法")
        } catch (e: InvalidKeySpecException) {
            throw Exception("私钥非法")
        } catch (e: NullPointerException) {
            throw Exception("私钥数据为空")
        }

    }

    /**
     * 公钥加密过程
     *
     * @param publicKey
     * 公钥
     * @param plainTextData
     * 明文数据
     * @return
     * @throws Exception
     * 加密过程中的异常信息
     */
    @Throws(Exception::class)
    fun encrypt(publicKey: RSAPublicKey?, plainTextData: ByteArray): ByteArray? {
        if (publicKey == null) {
            throw Exception("加密公钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA")
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher!!.init(Cipher.ENCRYPT_MODE, publicKey)
            return cipher.doFinal(plainTextData)
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此加密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("加密公钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("明文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("明文数据已损坏")
        }

    }

    /**
     * 私钥加密过程
     *
     * @param privateKey
     * 私钥
     * @param plainTextData
     * 明文数据
     * @return
     * @throws Exception
     * 加密过程中的异常信息
     */
    @Throws(Exception::class)
    fun encrypt(privateKey: RSAPrivateKey?, plainTextData: ByteArray): ByteArray? {
        if (privateKey == null) {
            throw Exception("加密私钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA")
            cipher!!.init(Cipher.ENCRYPT_MODE, privateKey)
            return cipher.doFinal(plainTextData)
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此加密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("加密私钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("明文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("明文数据已损坏")
        }

    }

    /**
     * 私钥解密过程
     *
     * @param privateKey
     * 私钥
     * @param cipherData
     * 密文数据
     * @return 明文
     * @throws Exception
     * 解密过程中的异常信息
     */
    @Throws(Exception::class)
    fun decrypt(privateKey: RSAPrivateKey?, cipherData: ByteArray): ByteArray? {
        if (privateKey == null) {
            throw Exception("解密私钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher!!.init(Cipher.DECRYPT_MODE, privateKey)
            return cipher.doFinal(cipherData)
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此解密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("解密私钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("密文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("密文数据已损坏")
        }

    }

    /**
     * 公钥解密过程
     *
     * @param publicKey
     * 公钥
     * @param cipherData
     * 密文数据
     * @return 明文
     * @throws Exception
     * 解密过程中的异常信息
     */
    @Throws(Exception::class)
    fun decrypt(publicKey: RSAPublicKey?, cipherData: ByteArray): ByteArray? {
        if (publicKey == null) {
            throw Exception("解密公钥为空, 请设置")
        }
        var cipher: Cipher? = null
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA")
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher!!.init(Cipher.DECRYPT_MODE, publicKey)
            return cipher.doFinal(cipherData)
        } catch (e: NoSuchAlgorithmException) {
            throw Exception("无此解密算法")
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return null
        } catch (e: InvalidKeyException) {
            throw Exception("解密公钥非法,请检查")
        } catch (e: IllegalBlockSizeException) {
            throw Exception("密文长度非法")
        } catch (e: BadPaddingException) {
            throw Exception("密文数据已损坏")
        }

    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data
     * 输入数据
     * @return 十六进制内容
     */
    fun byteArrayToString(data: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (i in data.indices) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] and 0xf0.toByte()).toInt().ushr(4)])
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] and 0x0f.toByte()).toInt()])
            if (i < data.size - 1) {
                stringBuilder.append(' ')
            }
        }
        return stringBuilder.toString()
    }
}  