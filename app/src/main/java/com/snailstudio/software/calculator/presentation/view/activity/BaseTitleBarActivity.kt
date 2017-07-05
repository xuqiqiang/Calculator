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
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.snailstudio.library.baseview.PopMenu
import com.snailstudio.library.baseview.SystemBarTintManager
import com.snailstudio.library.baseview.ThemeManager
import com.snailstudio.library.baseview.ToastMaster
import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.databinding.ActivityTitlebarLayoutBinding
import com.snailstudio.software.calculator.presentation.internal.di.HasComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.DaggerCalculatorComponent
import com.snailstudio.software.calculator.presentation.view.fragment.BaseBindingFragment
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onTouch
import org.jetbrains.anko.textColor

/**
 * Created by xuqiqiang on 2016/04/17.
 */
abstract class BaseTitleBarActivity : BaseBindingActivity<ActivityTitlebarLayoutBinding>(), HasComponent<CalculatorComponent> {

    internal var calculatorComponent: CalculatorComponent? = null

    lateinit var mFragment: Fragment

    private var showMore: Boolean = false
    protected var mPopMenu: PopMenu? = null

    companion object {
        val RESULT_UNKNOWN_ERROR = -99
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityTitlebarLayoutBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_titlebar_layout)
    }

    override fun initView(savedInstanceState: Bundle?) {
        //this.getApplicationComponent()?.inject(this)
        initializeInjector()
        if (savedInstanceState == null) {
            SystemBarTintManager.initSystemBar(this)
            mFragment = initFragment()
            addFragment(R.id.fragmentContainer, mFragment)
            initTitleBar()
        }
    }

    abstract fun initFragment(): Fragment

    protected fun initTitleBar() {

        with(mBinding) {

            val mThemeManager = ThemeManager.getInstance()
            if (!mThemeManager.isDefaultTheme) {
                baseTitleBarLayout.setBackgroundColor(resources.getColor(
                        mThemeManager.background_color))
            }
            title = title
            btnTitleBack.onClick {
                onTitleBack()
            }
            initPopMenu()

            if (!showMore) {
                btnTitleMore.visibility = View.GONE
            } else {
                btnTitleMore.onClick {
                    v ->
                    mPopMenu!!.showAsDropDown(v)
                }
            }
        }
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        mBinding.tvTitle.text = title
    }

    protected fun setOnTitleTextClickListener(listener: View.OnClickListener) {
        with(mBinding.tvTitle) {
            onTouch { _,
                      event ->
                if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                    textColor = android.graphics.Color.RED
                } else if (event.action == android.view.MotionEvent.ACTION_UP) {
                    textColor = android.graphics.Color.WHITE
                }
                false
            }
            setOnClickListener(listener)
        }
    }

    protected fun onTitleBack() {
        if (mFragment !is BaseBindingFragment<*>)
            finish()
        return (mFragment as BaseBindingFragment<*>).onTitleBack()
    }

    protected fun initPopMenu() {
        val popMenu_itemList = ArrayList<String>()
        val popMenu_itemListDrawable = ArrayList<Int>()
        val popMenu_listener = initPopmenu(popMenu_itemList,
                popMenu_itemListDrawable)
        if (popMenu_itemList.isEmpty()) {
            showMore = false
        } else {
            showMore = true
            mPopMenu = PopMenu(this, PopMenu.STYLE_BLUE)
            mPopMenu!!.setItems(popMenu_itemList)
            mPopMenu!!.setItemsDrawable(popMenu_itemListDrawable)
            mPopMenu!!.setOnItemClickListener(popMenu_listener)
        }
    }

    open fun initPopmenu(
            popMenu_itemList: ArrayList<String>,
            popMenu_itemListDrawable: ArrayList<Int>): PopMenu.OnPopMenuItemClickListener? {
        if (mFragment !is BaseBindingFragment<*>)
            return null
        return (mFragment as BaseBindingFragment<*>).initPopmenu(this, popMenu_itemList,
                popMenu_itemListDrawable)
    }

    fun setPopMenu(popMenu: PopMenu?) {
        this.mPopMenu = popMenu
        if (popMenu != null) {
            showMore = true
            with(mBinding.btnTitleMore) {
                visibility = View.VISIBLE
                onClick {
                    v ->
                    mPopMenu!!.showAsDropDown(v)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        LogUtils.d("requestCode : " + requestCode + ", resultCode : "
                + resultCode)
        if (resultCode == RESULT_UNKNOWN_ERROR)
            ToastMaster.showToast(this, R.string.unknown_error)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (showMore) {
                mPopMenu!!.showAsDropDown(mBinding.btnTitleMore)
                return true
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onTitleBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initializeInjector() {
        this.calculatorComponent = DaggerCalculatorComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
    }

    override fun getComponent() = calculatorComponent!!

}
