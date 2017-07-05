/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view

import android.content.Context
import android.widget.EditText

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a calculator.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface CalculatorView {

    fun getEditText(): EditText?

    /**
     * Get a [Context].
     */
    fun context(): Context?

    /**
     * Show an message

     * @param message A string representing an message.
     */
    fun showMessage(message: String)
}
