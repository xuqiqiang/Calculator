/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.executor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MainThread (UI Thread) implementation based on a [Scheduler]
 * which will execute actions on the Android UI thread
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class UIThread @Inject
internal constructor() : PostExecutionThread {

    override fun getScheduler(): Scheduler = AndroidSchedulers.mainThread()
}