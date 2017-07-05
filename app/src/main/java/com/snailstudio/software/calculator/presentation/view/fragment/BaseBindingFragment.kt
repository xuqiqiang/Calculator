/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.fragment

import android.app.Fragment
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.snailstudio.library.baseview.PopMenu
import com.snailstudio.software.calculator.presentation.internal.di.HasComponent

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseBindingFragment<B : ViewDataBinding> : Fragment() {
    lateinit var mBinding: B

    init {
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = createDataBinding(inflater, container, savedInstanceState)
        initView()
        return mBinding.root
    }

    abstract fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): B

    abstract fun initView()

    open fun onTitleBack() {
        activity.finish()
    }

    open fun initPopmenu(context: Context,
                         popMenu_itemList: ArrayList<String>,
                         popMenu_itemListDrawable: ArrayList<Int>): PopMenu.OnPopMenuItemClickListener? {
        return null
    }

    /**
     * Shows a [Toast] message.

     * @param message An string representing a message to be shown.
     */
    fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).getComponent())
    }
}