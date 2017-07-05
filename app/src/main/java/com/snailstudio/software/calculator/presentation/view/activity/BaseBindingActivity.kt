/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.activity

import android.app.Fragment
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.snailstudio.library.utils.ScreenInfo
import com.snailstudio.software.calculator.presentation.AndroidApplication
import com.snailstudio.software.calculator.presentation.internal.di.modules.ActivityModule

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenInfo.createInstance(this)

        mBinding = createDataBinding(savedInstanceState)
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun createDataBinding(savedInstanceState: Bundle?): B

    /**
     * Adds a [Fragment] to this activity's layout.

     * @param containerViewId The container view to where add the fragment.
     * *
     * @param fragment        The fragment to be added.
     */
    open fun addFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = this.fragmentManager.beginTransaction()
        fragmentTransaction.add(containerViewId, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Get the Main Application component for dependency injection.

     * @return [com.snailstudio.software.calculator.internal.di.components.ApplicationComponent0]
     */
    protected fun getApplicationComponent()
            = (application as AndroidApplication).applicationComponent

    /**
     * Get an Activity module for dependency injection.

     * @return [com.snailstudio.software.calculator.internal.di.modules.ActivityModule0]
     */
    protected fun getActivityModule() = ActivityModule(this)

}