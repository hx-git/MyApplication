/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cali.libcore.http

import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.Request

import java.io.File
import java.util.TreeMap

/**
 * 使用okhttp
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:04
 */
class OKHttpUpdateHttpService : IUpdateHttpService {

    override fun asyncGet(url: String, params: Map<String, Any>, callBack: IUpdateHttpService.Callback) {
        OkHttpUtils.get()
            .url(url)
            .params(transform(params))
            .build()
            .execute(object : StringCallback() {
                override fun onError(call: Call, e: Exception, id: Int) {
                    callBack.onError(e)
                }

                override fun onResponse(response: String, id: Int) {
                    callBack.onSuccess(response)
                }
            })
    }

    override fun asyncPost(url: String, params: Map<String, Any>, callBack: IUpdateHttpService.Callback) {
        //这里默认post的是Form格式，使用json格式的请修改 post -> postString
        OkHttpUtils.post()
            .url(url)
            .params(transform(params))
            .build()
            .execute(object : StringCallback() {
                override fun onError(call: Call, e: Exception, id: Int) {
                    callBack.onError(e)
                }

                override fun onResponse(response: String, id: Int) {
                    callBack.onSuccess(response)
                }
            })
    }

    override fun download(
        url: String,
        path: String,
        fileName: String,
        callback: IUpdateHttpService.DownloadCallback
    ) {
        OkHttpUtils.get()
            .url(url)
            .build()
            .execute(object : FileCallBack(path, fileName) {
                override fun inProgress(progress: Float, total: Long, id: Int) {
                    callback.onProgress(progress, total)
                }

                override fun onError(call: Call, e: Exception, id: Int) {
                    callback.onError(e)
                }

                override fun onResponse(response: File, id: Int) {
                    callback.onSuccess(response)
                }

                override fun onBefore(request: Request?, id: Int) {
                    super.onBefore(request, id)
                    callback.onStart()
                }
            })
    }

    override fun cancelDownload(url: String) {

    }

    private fun transform(params: Map<String, Any>): Map<String, String> {
        val map = TreeMap<String, String>()
        for ((key, value) in params) {
            map[key] = value.toString()
        }
        return map
    }


}