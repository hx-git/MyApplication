package com.cali.common.kt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@Throws(Exception::class)
fun URL.get(block:(result:String) -> Unit){
    val url = this
    GlobalScope.launch(Dispatchers.IO){
        val conn = url.openConnection() as HttpsURLConnection
        if (conn.responseCode == 200) {
            val ins = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (-1 != (ins.read(buffer)).also { len = it }) {
                out.write(buffer, 0, len)
                out.flush()
            }
            launch(Dispatchers.Main) {
                block(out.toString("utf-8"))
            }
        }
    }

}