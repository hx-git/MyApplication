package com.cali.libcore.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface IActivity {
    fun initView(savedInstanceState: Bundle?):Int
    fun initData(savedInstanceState: Bundle?)
}

interface IFragment{
    fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View
    fun initData(savedInstanceState: Bundle?)
}