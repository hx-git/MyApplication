package com.cali.libcore.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cali.common.R
import com.cali.libcore.view.loading.LoadingController
import com.cali.libcore.view.xrecyclerview.XRecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.android.synthetic.main.fragment_list_base.*

abstract class BaseListFragment:BaseFragment(){

    private lateinit var layoutManager: LinearLayoutManager
    var page:Int = 1
    private lateinit var mXRecyclerView: XRecyclerView
    val loadingController:LoadingController by lazy {
        LoadingController.Builder(mContext,mXRecyclerView)
            .setOnErrorRetryClickListener {

            }
            .setEmptyViewImageResource(getEmptyViewImageResource())
            .setOnEmptyTodoClickListener(onEmptyText()) {
                onEmptyTodo()
            }
            .setLoadingMessage("正在加载中")
            .build()
    }

    //使用final 禁止复写
    final override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun getLayoutId() = R.layout.fragment_list_base

    open fun getEmptyViewImageResource() = R.drawable.ic_default_loading_empty

    //数据为空文本显示
    open fun onEmptyText() = "没有数据"

    //数据为空todo
    open fun onEmptyTodo() {

    }

    final override fun initData(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(context)
        mXRecyclerView = x_recycler
        mXRecyclerView.layoutManager = layoutManager
        loadingAdapter = getAdapter()
        //????
        loadingAdapter.setHasStableIds(true)
        loadingAdapter.bindToRecyclerView(mXRecyclerView)

        mXRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener{
            override fun onRefresh() {
                page = 1
                getPageData()
            }

            override fun onLoadMore() {
                if (needLoadMore()) {
                    page ++
                    getPageData()
                }
            }
        })
        mXRecyclerView.setLoadingMoreEnabled(true)
        mXRecyclerView.setPullRefreshEnabled(true)
        getPageData()
    }

    abstract fun getPageData()
    //判断是否需要基类实现加载更多
    open fun needLoadMore() = true

    fun loadDataComplete() {
        if (page == 1) {
            loadingController.dismissLoading()
        }
        mXRecyclerView.loadMoreComplete()
        mXRecyclerView.refreshComplete()
    }

    lateinit var loadingAdapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>
    abstract fun getAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

}