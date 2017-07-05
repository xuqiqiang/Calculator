/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.internal.di

//import java.lang.annotation.Retention
//import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memorized in the
 * correct component.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity
