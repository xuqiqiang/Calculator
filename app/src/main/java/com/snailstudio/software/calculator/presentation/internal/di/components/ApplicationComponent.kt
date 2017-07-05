package com.snailstudio.software.calculator.presentation.internal.di.components

import android.content.Context
import com.snailstudio.software.calculator.data.entity.calculator.Speaker
import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import com.snailstudio.software.calculator.presentation.internal.di.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

/**
 * A component whose lifetime is the life of the application.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    //Exposed to sub-graphs.
    fun context(): Context

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread

    fun speaker(): Speaker

    fun calculatorRepository(): CalculatorRepository
}
