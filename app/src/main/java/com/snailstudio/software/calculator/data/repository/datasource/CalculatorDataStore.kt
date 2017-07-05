/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.repository.datasource

import com.snailstudio.software.calculator.data.entity.Value
import com.snailstudio.software.calculator.domain.interactor.DeleteSymbol
import com.snailstudio.software.calculator.domain.interactor.SaveReport
import com.snailstudio.software.calculator.domain.interactor.SaveSymbol
import com.snailstudio.software.calculator.presentation.model.Symbol
import io.reactivex.Observable

/**
 * Interface that represents a data store from where data is retrieved.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface CalculatorDataStore {
    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, [String].
     */
    fun distinguish(input: String): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, [String].
     */
    fun calculate(input: String): Observable<Value>

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    fun getSymbols(): Observable<List<Symbol>>

    /**
     * Get an [Observable] which will emit a [SaveSymbol.Params].
     *
     * @param params, [SaveSymbol.Params].
     */
    fun checkSymbol(params: SaveSymbol.Params?): Observable<Value>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param params, [SaveSymbol.Params].
     */
    fun saveSymbol(params: SaveSymbol.Params, result: String): String

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param Void .
     */
    fun checkSymbol(name: String?,
                    content: String?,
                    type: Int?,
                    id: Int?,
                    parameterNumber: Int?): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param params, [SaveSymbol.Params].
     */
    fun deleteSymbol(params: DeleteSymbol.Params): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param params, [SaveSymbol.Params].
     */
    fun saveReport(params: SaveReport.Params): Observable<String>


    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    fun getReports(): Observable<String>
}
