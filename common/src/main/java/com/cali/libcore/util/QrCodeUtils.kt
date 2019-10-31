package com.cali.libcore.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Environment
import com.cali.common.kt.debugLogInfo
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Dinosa on 2018/4/12.
 * 二维码的生成工具；
 */

object QrCodeUtils {


    /**
     * 生成二维码不缓存
     * @param content
     * @param width
     * @param height
     * @return
     */
    fun generateQrBitmap(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints = HashMap<EncodeHintType, String>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000
                    } else {
                        pixels[i * width + j] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
            return bitmap
        } catch (e: Exception) {
            e.toString().debugLogInfo()
        }

        return null
    }


    /**
     * 生成一个二维码的操作；
     * @param content
     * @param width
     * @param height
     * @return
     */
    fun generateBitmap(content: String, width: Int, height: Int): Bitmap? {

        val qrCodeBitmap = getQrCodeBitmap(content)
        if (qrCodeBitmap != null) {
            return qrCodeBitmap
        }

        val qrCodeWriter = QRCodeWriter()
        val hints = HashMap<EncodeHintType, String>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000
                    } else {
                        pixels[i * width + j] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)

            //这里我们要做的一个操作就是缓存二维码；
            //将二维码存储起来；
            saveQrCodeBitmap(bitmap, content)

            return bitmap
        } catch (e: Exception) {
            e.toString().debugLogInfo()
        }

        return null
    }

    private fun getQrCodeBitmap(qrCodeUrl: String): Bitmap? {

        //这里我们在生成二维码的时候，我们要做的一个操作就是将二维码换缓存起来；

        val storeQrCodeFile = getStoreQrCodeFile(qrCodeUrl)
        if (storeQrCodeFile.exists() && storeQrCodeFile.isFile) {
            //这里返回对应的Bitmap对象；
            println("来自缓存的二维码操作；")
            return BitmapFactory.decodeFile(storeQrCodeFile.absolutePath)
        }

        return null
    }

    @Throws(FileNotFoundException::class)
    fun saveQrCodeBitmap(bitmap: Bitmap, qrCodeUrl: String) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(getStoreQrCodeFile(qrCodeUrl)))
    }

    /**
     * 返回存储二维码的路径
     * @param qrCodeUrl
     * @return
     */
    private fun getStoreQrCodeFile(qrCodeUrl: String): File {

        val storePath = Environment.getExternalStorageDirectory().absoluteFile.toString() + "/youbu"
        val fileName = FormatUtils.md5(qrCodeUrl)

        val storeQrCodeDir = File(storePath)
        if (!storeQrCodeDir.exists() || storeQrCodeDir.isFile) {
            storeQrCodeDir.mkdirs()
        }

        return File(storeQrCodeDir, fileName)
    }

    /**
     * 创建一个二维码的对象；
     * @return
     */
    fun createBitmap(context: Context, bgDrawable: Drawable, qrcodeBitmap: Bitmap): Bitmap {

        val resources = context.resources

        val w = bgDrawable.intrinsicWidth
        val h = bgDrawable.intrinsicHeight
        bgDrawable.setBounds(0, 0, w, h)

        // 取 drawable 的颜色格式
        val config = if (bgDrawable.opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565

        //这是一个空的Bitmap对象的；
        val bitmap = Bitmap.createBitmap(w, h, config)
        //创建要给画布对象；
        val canvas = Canvas(bitmap)
        //画一个背景
        bgDrawable.draw(canvas)


        val size = UIUtils.dip2px(context, 170f)

        val left = (bgDrawable.intrinsicWidth - size) / 2
        val top = (bgDrawable.intrinsicHeight - size) / 2 - UIUtils.dip2px(context, 10f)

        //画二维码；
        canvas.drawBitmap(qrcodeBitmap, left.toFloat(), top.toFloat(), Paint())

        return bitmap
    }
}
