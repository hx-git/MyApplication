package com.cali.libcore.http

import com.cali.libcore.base.ContextHolder
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.diskconverter.GsonDiskConverter
import java.io.File

class CacheManagement {
    companion object{
        val rxCache :RxCache? = RxCache.Builder()
            .appVersion(1)
            .diskDir(File(ContextHolder.application.cacheDir.path + File.separator
            + "data-cache"))
            .diskConverter(GsonDiskConverter())
            .memoryMax(2 * 1024 * 1024)
            .diskMax(20 * 1024 * 1024)
            .build()
    }
}