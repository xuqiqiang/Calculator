/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view.activity

import android.app.Fragment
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.ActivityMainBinding
import com.snailstudio.software.calculator.presentation.internal.di.HasComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.DaggerCalculatorComponent
import com.snailstudio.software.calculator.presentation.view.fragment.CalculatorFragment
import org.jetbrains.anko.onClick

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class MainActivity : BaseBindingActivity<ActivityMainBinding>(),
        HasComponent<CalculatorComponent>, NavigationView.OnNavigationItemSelectedListener {

    internal var mCalculatorFragment: CalculatorFragment? = null

    internal var calculatorComponent: CalculatorComponent? = null

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun initView(savedInstanceState: Bundle?) {
        //this.getApplicationComponent()?.inject(this)
        initializeInjector()
        if (savedInstanceState == null) {
            mCalculatorFragment = CalculatorFragment()
            addFragment(R.id.fragmentContainer, mCalculatorFragment as Fragment)
        }

        with(mBinding) {
            val toggle = ActionBarDrawerToggle(
                    this@MainActivity, drawerLayout, null,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navView.getHeaderView(0).onClick {
                val uri = Uri
                        .parse(getString(R.string.website))
                val it = Intent(Intent.ACTION_VIEW, uri)
                startActivity(it)
            }
            navView.setNavigationItemSelectedListener(this@MainActivity)
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (mCalculatorFragment!!.onNavigationItemSelected(item.itemId))
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initializeInjector() {
        this.calculatorComponent = DaggerCalculatorComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
    }

    override fun getComponent() = calculatorComponent!!

}
