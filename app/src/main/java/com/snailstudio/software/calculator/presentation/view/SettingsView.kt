/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view

import android.app.Activity

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of settings.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface SettingsView {

    /**
     * Get a [Activity].
     */
    fun context(): Activity?

    fun refreshView(CbAutoSaveChecked: Boolean, vibratorTimeEnabled : Boolean)
}
