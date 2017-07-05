/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.domain.interactor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a result of Calculator.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class Calculate @Inject
internal constructor(private val calculatorRepository: CalculatorRepository, threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread)
    : UseCase<String, Calculate.Params>(threadExecutor, postExecutionThread) {
    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    override fun buildUseCaseObservable(params: Params?)
            = this.calculatorRepository.calculate(params!!.input)

    class Params private constructor(val input: String) {
        companion object {

            fun forUser(input: String): Params {
                return Params(input)
            }
        }
    }
}
