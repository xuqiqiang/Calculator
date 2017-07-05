package com.snailstudio.software.calculator.presentation.internal.di.components

import android.app.Activity
import com.snailstudio.software.calculator.presentation.internal.di.PerActivity
import com.snailstudio.software.calculator.presentation.internal.di.modules.ActivityModule

import dagger.Component

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * [PerActivity]
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    //Exposed to sub-graphs.
    fun activity(): Activity
}
