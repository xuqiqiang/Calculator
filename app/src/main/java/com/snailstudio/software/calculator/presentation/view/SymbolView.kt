/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.view

import android.app.Activity
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of [Symbol].
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface SymbolView {

    fun getEditText(): EditText?

    fun getNameEditText(): EditText?

    fun getNumSeekBar(): SeekBar?

    fun getNumTextView(): TextView?

    fun getFormTextView(): TextView?

    /**
     * Get a [Activity].
     */
    fun context(): Activity?

    /**
     * Show an message

     * @param message A string representing an message.
     */
    fun showMessage(message: String)

}
