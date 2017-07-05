package com.snailstudio.software.calculator.domain.interactor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import com.snailstudio.software.calculator.presentation.model.Symbol
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * deleting a [Symbol].
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class DeleteSymbol @Inject
internal constructor(private val calculatorRepository: CalculatorRepository,
                     threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread)
    : UseCase<String, DeleteSymbol.Params>(threadExecutor, postExecutionThread) {
    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    override fun buildUseCaseObservable(params: Params?)
            = this.calculatorRepository.deleteSymbol(params!!)

    class Params constructor(val symbol: Symbol?) {
        companion object {
            fun forUser(symbol: Symbol?): Params {
                return Params(symbol)
            }
        }
    }
}
