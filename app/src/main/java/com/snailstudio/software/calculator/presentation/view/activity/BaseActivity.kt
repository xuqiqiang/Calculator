/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.activity

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.ActivityLayoutBinding
import com.snailstudio.software.calculator.presentation.internal.di.HasComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.DaggerCalculatorComponent

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseActivity : BaseBindingActivity<ActivityLayoutBinding>(), HasComponent<CalculatorComponent> {

    internal var calculatorComponent: CalculatorComponent? = null

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityLayoutBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_layout)
    }

    override fun initView(savedInstanceState: Bundle?) {
        //this.getApplicationComponent()?.inject(this)
        initializeInjector()
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, initFragment())
        }
    }

    abstract fun initFragment(): Fragment

    private fun initializeInjector() {
        this.calculatorComponent = DaggerCalculatorComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
    }

    override fun getComponent() = calculatorComponent!!

}
