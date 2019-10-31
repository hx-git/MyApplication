package com.cali.libcore.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cali.common.R
import com.cali.common.kt.unregister
import com.cali.libcore.view.ToolbarHelper
import me.yokeyword.fragmentation.SupportFragment

abstract class BaseFragment:SupportFragment(),IToolbarView,IFragment{

    protected lateinit var mContext: Context
    protected val currentActivity by lazy {
        requireActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context.applicationContext
    }

    var mToolbarHelper:ToolbarHelper? = null

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView(inflater,container,savedInstanceState)
        return if (hasToolbar()) {
            mToolbarHelper = ToolbarHelper(view.context,view).apply {
                if (hasBackIcon()) {
//                    resetToolbarHeight()
                    toolbar.apply {
                        setNavigationIcon(R.drawable.ic_read_back)
                        setNavigationOnClickListener {
                            pop()
                        }
                    }
                }
            }
            mToolbarHelper?.contentView
        }else{
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
    }

    override fun hasBackIcon() = true

    override fun hasToolbar() = false


    fun startParentParentBrotherFragment(fragment: SupportFragment) {
        val mainFragment = parentFragment?.parentFragment as? SupportFragment
        mainFragment?.start(fragment,SINGLETASK)
    }


    fun startParentBrotherFragment(fragment: SupportFragment) {
        val mainFragment = parentFragment as? SupportFragment
        mainFragment?.start(fragment,SINGLETASK)
    }


    override fun onDestroy() {
        super.onDestroy()
        this.unregister()
    }

}