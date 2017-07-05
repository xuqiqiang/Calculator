package com.snailstudio.software.calculator.presentation.internal.di.components

import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.internal.di.modules.ActivityModule
import com.snailstudio.software.calculator.presentation.internal.di.modules.CalculatorModule
import com.snailstudio.software.calculator.presentation.presenter.CalculatorPresenter
import com.snailstudio.software.calculator.presentation.presenter.ReportsPresenter
import com.snailstudio.software.calculator.presentation.presenter.SettingsPresenter
import com.snailstudio.software.calculator.presentation.presenter.SymbolPresenter
import com.snailstudio.software.calculator.presentation.view.fragment.CalculatorFragment
import com.snailstudio.software.calculator.presentation.view.fragment.ReportsFragment
import com.snailstudio.software.calculator.presentation.view.fragment.SettingsFragment
import com.snailstudio.software.calculator.presentation.view.fragment.SymbolFragment
import dagger.Component

/**
 * A scope [PerActivity] component.
 * Injects user specific Fragments.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class, CalculatorModule::class))
interface CalculatorComponent : ActivityComponent {
    fun inject(calculatorFragment: CalculatorFragment)
    fun inject(symbolFragment: SymbolFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsFragment: ReportsFragment)

    fun inject(calculatorPresenter: CalculatorPresenter)
    fun inject(symbolPresenter: SymbolPresenter)
    fun inject(settingsPresenter: SettingsPresenter)
    fun inject(settingsFragment: ReportsPresenter)
}
