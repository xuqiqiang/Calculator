/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.domain.executor

import com.snailstudio.software.calculator.domain.interactor.UseCase

import java.util.concurrent.Executor

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 * [UseCase] out of the UI thread.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface ThreadExecutor : Executor
