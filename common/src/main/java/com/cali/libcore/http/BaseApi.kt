package com.cali.libcore.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface BaseApi {
    @GET
    fun getImageByUrl(): Call<ResponseBody>
}