package com.snailstudio.software.calculator.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snailstudio.software.calculator.databinding.FragmentSettingsBinding
import com.snailstudio.software.calculator.presentation.internal.di.components.CalculatorComponent
import com.snailstudio.software.calculator.presentation.presenter.SettingsPresenter
import com.snailstudio.software.calculator.presentation.view.SettingsView
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.onClick
import javax.inject.Inject

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class SettingsFragment
    : BaseBindingFragment<FragmentSettingsBinding>(), SettingsView {


    @Inject
    lateinit var mSettingsPresenter: SettingsPresenter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?,
                                   savedInstanceState: Bundle?): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        with(mBinding) {
            settingAutoSave.onClick {
                settingCbAutoSave.isChecked = !settingCbAutoSave.isChecked
            }

            settingCbAutoSave.onCheckedChange { _, b ->
                mSettingsPresenter.onCbAutoSaveCheck(b)
            }

            settingPointSum.onClick {
                mSettingsPresenter.onButtonPointSumClick()
            }

            settingMake.onClick {
                mSettingsPresenter.onButtonMakeClick()
            }

            settingKeyRespond.onClick {
                mSettingsPresenter.onButtonKeyRespondClick()
            }

            settingVibratorTime.onClick {
                mSettingsPresenter.onButtonVibratorTimeClick()
            }
        }
    }

    override fun refreshView(CbAutoSaveChecked: Boolean,
                             vibratorTimeEnabled: Boolean) {
        with(mBinding) {
            settingCbAutoSave.isChecked = CbAutoSaveChecked
            settingVibratorTime.isEnabled = vibratorTimeEnabled
        }
    }

    /**
     * Get a [Context].
     */
    override fun context(): Activity = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(CalculatorComponent::class.java).inject(this)
        this.getComponent(CalculatorComponent::class.java).inject(mSettingsPresenter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mSettingsPresenter.setView(this)
        this.mSettingsPresenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        this.mSettingsPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mSettingsPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mSettingsPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mSettingsPresenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.mSettingsPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }


}
