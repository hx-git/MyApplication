package com.cali.common.kt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

//保存bitmap到图库
fun Bitmap.saveBitmapToMedia(context: Context,name:String) {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        context.toast("保存失败")
        return
    }
    val savePath = Environment.getExternalStorageDirectory().path + "/youbu/"
    val fileName = "$name.JPEG"
    val file = File(savePath + fileName)
    GlobalScope.launch {
        val isSuccess = doWithTryReturn {
            if (!file.exists()) {
                file.parentFile.mkdir()
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            this@saveBitmapToMedia.compress(Bitmap.CompressFormat.JPEG,100,fos)
            fos.flush()
            fos.close()
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://${savePath+fileName}")))
        }
        launch(Dispatchers.Main){
            val tip = if(isSuccess) "保存成功" else "保存失败"
            context.toast(tip)
        }
    }

}