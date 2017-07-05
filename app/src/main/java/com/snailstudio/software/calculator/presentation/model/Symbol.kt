/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.model

/**
 * Created by xuqiqiang on 2016/04/17.
 */
data class Symbol(
        val id: Int,
        val type: Int,
        val name: String,
        val showName: String,
        val help: String,
        val isNative: Boolean
)