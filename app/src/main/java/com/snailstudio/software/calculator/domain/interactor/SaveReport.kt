package com.snailstudio.software.calculator.domain.interactor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * saving a collection of all reports.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class SaveReport @Inject
internal constructor(private val calculatorRepository: CalculatorRepository,
                     threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread)
    : UseCase<String, SaveReport.Params>(threadExecutor, postExecutionThread) {
    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    override fun buildUseCaseObservable(params: Params?)
            = this.calculatorRepository.saveReport(params!!)

    class Params private constructor(val input: String, val result: String) {
        companion object {

            fun forUser(input: String, result: String): Params {
                return Params(input, result)
            }
        }
    }
}
