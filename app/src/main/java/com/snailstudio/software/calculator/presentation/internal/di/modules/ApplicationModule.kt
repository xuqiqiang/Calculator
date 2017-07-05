package com.snailstudio.software.calculator.presentation.internal.di.modules

import android.content.Context
import com.snailstudio.software.calculator.data.entity.calculator.Speaker
import com.snailstudio.software.calculator.data.executor.JobExecutor
import com.snailstudio.software.calculator.data.executor.UIThread
import com.snailstudio.software.calculator.data.repository.CalculatorDataRepository
import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import com.snailstudio.software.calculator.presentation.AndroidApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context = this.application

    @Provides
    @Singleton
    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    internal fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread = uiThread

    @Provides
    @Singleton
    internal fun provideSpeaker(context: Context): Speaker = Speaker(context)

    @Provides
    @Singleton
    internal fun provideCalculatorRepository(calculatorDataRepository: CalculatorDataRepository): CalculatorRepository
            = calculatorDataRepository
}
