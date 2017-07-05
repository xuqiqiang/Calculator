package com.snailstudio.software.calculator.domain.interactor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import com.snailstudio.software.calculator.presentation.model.Symbol
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a collection of all [Symbol].
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class GetSymbols @Inject
internal constructor(private val calculatorRepository: CalculatorRepository,
                     threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread)
    : UseCase<List<Symbol>, Any>(threadExecutor, postExecutionThread) {
    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    override fun buildUseCaseObservable(params: Any?) = this.calculatorRepository.getSymbols()

}
