/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.internal.di

/**
 * Interface representing a contract for clients that contains a component for dependency injection.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface HasComponent<out C> {
    fun getComponent(): C
}
