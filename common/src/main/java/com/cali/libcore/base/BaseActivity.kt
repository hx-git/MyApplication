package com.cali.libcore.base

import android.os.Bundle
import com.cali.libcore.view.dialog.LoadingDialog
import me.yokeyword.fragmentation.SupportActivity

abstract class BaseActivity:SupportActivity(),IActivity{
    val loadingDialog by lazy {
        LoadingDialog(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val contentId = initView(savedInstanceState)
            if (contentId != 0) {
                setContentView(contentId)
            }
        }catch (e:Exception) {
            e.stackTrace
        }
        initData(savedInstanceState)
    }

}