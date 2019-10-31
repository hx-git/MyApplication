package com.cali.libcore.util

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cali.common.R
import com.zchu.log.Logger

import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Dinosa on 2018/1/22.
 *
 * 这里是我们加载图片的一个工具类；
 * 方便做统一的处理；
 */

object ImageUtils {


    fun loadImage(activity: Activity?, url: String, view: ImageView?) {

        if (activity == null || activity.isFinishing || TextUtils.isEmpty(url) || view == null) {
            return
        }

        val colorDrawable = ColorDrawable(activity.resources.getColor(R.color.defaultImgColor))
        Glide.with(activity).load(url).dontAnimate().placeholder(colorDrawable).into(view)
    }

    fun loadImage(fragment: Fragment?, url: String, view: ImageView?) {

        if (fragment == null || fragment.isRemoving || TextUtils.isEmpty(url) || view == null) {
            return
        }

        val colorDrawable = ColorDrawable(fragment.activity!!.resources.getColor(R.color.defaultImgColor))
        Glide.with(fragment.activity!!).load(url).dontAnimate().placeholder(colorDrawable).into(view)
    }

    fun loadImage(context: Context?, url: String?, view: ImageView?) {

        if (context == null || TextUtils.isEmpty(url) || view == null) {
            return
        }

        val colorDrawable = ColorDrawable(context.resources.getColor(R.color.defaultImgColor))
        Glide.with(context)
            .load(url)
            .dontAnimate()
            .placeholder(colorDrawable)
            .into(view)
    }


    fun getImgWH(urls: String): IntArray {
//        val execute = RetrofitManagement.instance.getService(BaseApi::class.java, urls)
//            .getImageByUrl()
//            .execute()
//
//        val bit = BitmapFactory.decodeStream(execute.body()?.byteStream())
//        "图片宽${bit.width} = ${bit.height}".debugLogInfo()
//        return intArrayOf()
        if (!urls.contains("http")) {
            return intArrayOf()
        }
        val url = URL(urls)

        val conn = url.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.connect()
        val input = conn.inputStream
        val image = BitmapFactory.decodeStream(input)

        val srcWidth = image.width      // 源图宽度
        val srcHeight = image.height    // 源图高度

        Logger.d("图片宽:$srcWidth = $srcHeight")

        val imgSize = IntArray(2)
        imgSize[0] = srcWidth
        imgSize[1] = srcHeight
        //释放资源
        image.recycle()
        input.close()
        conn.disconnect()
        return imgSize

    }
}
