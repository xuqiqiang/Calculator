/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.repository

import com.snailstudio.software.calculator.data.mapper.CalculatorDataMapper
import com.snailstudio.software.calculator.data.repository.datasource.CalculatorDataStoreImpl
import com.snailstudio.software.calculator.domain.interactor.DeleteSymbol
import com.snailstudio.software.calculator.domain.interactor.SaveReport
import com.snailstudio.software.calculator.domain.interactor.SaveSymbol
import com.snailstudio.software.calculator.domain.repository.CalculatorRepository
import com.snailstudio.software.calculator.presentation.model.Symbol
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [CalculatorRepository] for retrieving calculator data.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class CalculatorDataRepository
/**
 * Constructs a [CalculatorRepository].

 * @param calculatorDataStore [CalculatorDataStoreImpl].
 * *
 * @param calculatorDataMapper [CalculatorDataMapper].
 */
@Inject
internal constructor(private val calculatorDataStore: CalculatorDataStoreImpl,
                     private val calculatorDataMapper: CalculatorDataMapper
) : CalculatorRepository {

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    override fun distinguish(input: String): Observable<String> {
        return calculatorDataStore.distinguish(input)
    }

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    override fun calculate(input: String): Observable<String> {
        return calculatorDataStore.calculate(input).map(this.calculatorDataMapper::transform2Result)
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun getSymbols(): Observable<List<Symbol>> {
        return calculatorDataStore.getSymbols()
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun saveSymbol(params: SaveSymbol.Params): Observable<String> {
        return calculatorDataStore.checkSymbol(params)
                .map(calculatorDataMapper::transform2Input)
                .map({ result -> calculatorDataStore.saveSymbol(params, result!!) })
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun deleteSymbol(params: DeleteSymbol.Params): Observable<String> {
        return calculatorDataStore.deleteSymbol(params)
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun saveReport(params: SaveReport.Params): Observable<String> {
        return calculatorDataStore.saveReport(params)
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun getReports(): Observable<String> {
        return calculatorDataStore.getReports()
    }

}
