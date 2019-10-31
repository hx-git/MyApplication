package com.cali.common.kt

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.io.Closeable

class WindowLifeCycle(val context: Context):Application.ActivityLifecycleCallbacks,Closeable{

    private lateinit var activity:Activity

    init {
        if (context is Application) {
            context.registerActivityLifecycleCallbacks(this)
        }else{
            (context.applicationContext as Application).registerActivityLifecycleCallbacks(this)
        }
    }

    fun getCurrentActivity(): Activity {
        return activity
    }

    override fun onActivityPaused(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
        this.activity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        this.activity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        "监听activity生命周期".debugLogInfo()
        this.activity = activity
    }


    override fun close() {

    }

}


