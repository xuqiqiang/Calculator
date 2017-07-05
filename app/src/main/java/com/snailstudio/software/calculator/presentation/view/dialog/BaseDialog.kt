/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.databinding.ViewDataBinding
import android.os.Bundle

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseDialog<B : ViewDataBinding>
(protected val mContext: Context?, themeResId: Int) : Dialog(mContext, themeResId), DialogInterface.OnDismissListener {

    lateinit var mBinding: B

    protected var mCreated = false

    abstract fun createDataBinding(savedInstanceState: Bundle?): B

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun onRefresh()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = createDataBinding(savedInstanceState)
        setCanceledOnTouchOutside(false)
        initView(savedInstanceState)
        mCreated = true
    }

    override fun onStart() {
        super.onStart()
        setOnDismissListener(this)
    }

    override fun show() {
        if (mCreated) onRefresh()
        super.show()
    }

    fun close() {
        if (!isShowing) return
        dismiss()
    }
}