package com.cali.libcore.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cali.common.kt.unregister
import com.cali.common.R
import com.cali.libcore.http.RetrofitManagement
import com.cali.libcore.view.ToolbarHelper
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.yokeyword.fragmentation.SupportFragment

abstract class NoPresenterBaseFragment:SupportFragment(), IFragment ,IToolbarView{
    //管理rxjava
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    var mToolbarHelper:ToolbarHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView(inflater,container,savedInstanceState)
        return if (hasToolbar()) {
            mToolbarHelper = ToolbarHelper(view.context,view)
            if (hasBackIcon()) {
                mToolbarHelper?.toolbar?.apply {
                    setNavigationIcon(R.drawable.ic_read_back)
                    setNavigationOnClickListener {
                        pop()
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

    override fun hasToolbar() = false

    override fun hasBackIcon() = true


    fun startParentParentBrotherFragment(fragment: SupportFragment) {
        val mainFragment = parentFragment?.parentFragment as? SupportFragment
        mainFragment?.start(fragment)
    }

    fun startParentBrotherFragment(fragment: SupportFragment) {
        val mainFragment = parentFragment as? SupportFragment
        mainFragment?.start(fragment)
    }

    fun <T : Any> getService(kClass: Class<T>): T {
        return RetrofitManagement.instance.getService(kClass)
    }

    fun <T : Any?> scheduler(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                }.doFinally {

                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        this.unregister()
    }
}