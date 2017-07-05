/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation

import android.app.Application
import android.os.Environment
import com.snailstudio.library.baseview.ThemeManager
import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.library.logutils.config.LogConfig.newBuilder
import com.snailstudio.library.utils.Cache
import com.snailstudio.software.calculator.BuildConfig
import com.snailstudio.software.calculator.presentation.internal.di.components.ApplicationComponent
import com.snailstudio.software.calculator.presentation.internal.di.components.DaggerApplicationComponent
import com.snailstudio.software.calculator.presentation.internal.di.modules.ApplicationModule
import java.io.File

/**
 * Android Main Application
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class AndroidApplication : Application() {

    var applicationComponent: ApplicationComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        this.initializeInjector()
        Cache.initialize(this, "Calculator")
        ThemeManager.createInstance(this)

        val dirPath = Environment.getExternalStorageDirectory()
                .absolutePath + File.separator + "Calculator"
        LogUtils.initialize(newBuilder()
                .debug(BuildConfig.DEBUG)
                .enableWrite(this, dirPath)
                .reportCrash(true)
                .build())
    }

    private fun initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}
