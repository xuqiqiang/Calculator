package com.snailstudio.software.calculator.domain.interactor

import com.snailstudio.software.calculator.domain.executor.PostExecutionThread
import com.snailstudio.software.calculator.domain.executor.ThreadExecutor
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * saving a collection of all Symbol.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class SaveSymbol @Inject
internal constructor(private val calculatorRepository: CalculatorRepository,
                     threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread)
    : UseCase<String, SaveSymbol.Params>(threadExecutor, postExecutionThread) {
    /**
     * Builds an [Observable] which will be used when executing the current [UseCase].
     */
    override fun buildUseCaseObservable(params: Params?)
            = this.calculatorRepository.saveSymbol(params!!)

    class Params constructor(val name: String?,
                             val content: String?,
                             val type: Int?,
                             val id: Int?,
                             val parameterNumber: Int?) {
        companion object {
            fun forUser(name: String?,
                        content: String?,
                        type: Int?,
                        id: Int?,
                        parameterNumber: Int?): Params {
                return Params(name, content, type, id, parameterNumber)
            }
        }
    }
}
