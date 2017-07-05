package com.snailstudio.software.calculator.domain.repository

import com.snailstudio.software.calculator.domain.interactor.DeleteSymbol
import com.snailstudio.software.calculator.domain.interactor.SaveReport
import com.snailstudio.software.calculator.domain.interactor.SaveSymbol
import com.snailstudio.software.calculator.presentation.model.Symbol
import io.reactivex.Observable

/**
 * Interface that represents a Repository for getting [Symbol] related data.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface CalculatorRepository {
    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    fun distinguish(input: String): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    fun calculate(input: String): Observable<String>

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    fun getSymbols(): Observable<List<Symbol>>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    fun saveSymbol(params: SaveSymbol.Params): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    fun deleteSymbol(params: DeleteSymbol.Params): Observable<String>

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    fun saveReport(params: SaveReport.Params): Observable<String>

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    fun getReports(): Observable<String>
}
