/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.make
import com.snailstudio.software.calculator.data.entity.Data
import com.snailstudio.software.calculator.data.entity.Data.Companion.TYPE_MATRIX
import com.snailstudio.software.calculator.data.entity.Data.Companion.TYPE_NUMBER
import com.snailstudio.software.calculator.data.entity.Value
import com.snailstudio.software.calculator.data.entity.Value.Companion.CORRECT
import com.snailstudio.software.calculator.data.entity.Value.Companion.ERROR
import com.snailstudio.software.calculator.data.entity.Value.Companion.INFINITE
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.function
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.getConstant
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isCalcAllSymbol
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isCalcMainSymbol
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isCalcSymbol
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isConstant
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isInt
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isNum
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.rank
import com.snailstudio.software.calculator.data.exception.InfiniteException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The best calculator on the earth
 *
 * Created by xuqiqiang on 2016/06/26.
 */
@Singleton
class Calculator
@Inject
internal constructor() {

    // Handle '!'   e.g., 7!!!6 -> 7!3×6
    fun amendFactorials(a: ArrayList<Char>) {
        foreach(a, '!') {
            if (it < a.size - 1) {
                val symSum = getSuccessiveSymbolSum(a, it, '!')
                val nextIndex = it + symSum
                if (nextIndex < a.size) {
                    if (isNum(a[nextIndex]) || isConstant(a[nextIndex])) {
                        a.add(nextIndex, '×')
                    }
                }
                replaceField(a, it + 1, nextIndex - 1, ('0' + symSum))
            } else {
                a.add('1')
            }
        }
    }

    fun amendBrackets(a: ArrayList<Char>) {
        val leftSum = (0..a.size - 1).count { a[it] == '(' }
        val rightSum = (0..a.size - 1).count { a[it] == ')' }
        if (leftSum > rightSum) {
            for (i in rightSum..leftSum - 1) {
                a.add(')')
            }
        } else if (leftSum < rightSum) {
            for (i in leftSum..rightSum - 1) {
                a.add(0, '(')
            }
        }
    }

    fun amendIntervals(a: ArrayList<Char>) {
        if (a[0] == '.') { // e.g., .3 -> 0.3
            a.add(0, '0')
        }

        if (a[a.size - 1] == '.') { // e.g., 3. -> 3.0
            a.add('0')
        }

        foreach(a) {
            if (it < a.size - 1) {
                if (isNum(a[it]) && !isNum(a[it + 1])
                        && !isCalcSymbol(a[it + 1]) && a[it + 1] != ')'
                        && a[it + 1] != ',' && a[it + 1] != ';' && a[it + 1] != ']' // e.g., 5sin3 -> 5×sin3
                        || a[it] == ')' && a[it + 1] == '(' // e.g., (1+2)(2+3) -> (1+2)×(2+3)
                        || a[it] == ']' && a[it + 1] == '[' // e.g., [1,2][2;3] -> [1,2]×[2;3]
                        || isConstant(a[it]) && isNum(a[it + 1])) { // e.g., π3 -> π×3
                    a.add(it + 1, '×')
                } else if (!isNum(a[it]) && a[it + 1] == '.' // e.g., 1+.3 -> 1+0.3
                        || a[it] == '.' && !isNum(a[it + 1])) { // e.g., 3.+1 -> 3.0+1
                    a.add(it + 1, '0')
                }
            }
        }
    }

    fun getOriginalNumbers(a: ArrayList<Char>): ArrayList<Double> {
        val numbers = ArrayList<Double>()
        foreachStep(a) {
            var step: Int = 1
            if (isNum(a[it])) {
                val endInd = a.indexWhile(it) { isNum(it) }
                numbers.add(a.getNumber(it, endInd))
                step = endInd - it + 2
            } else if (isConstant(a[it])) {
                numbers.add(getConstant(a[it]))
                a[it] = '1'
            }
            step
        }
        return numbers
    }

    fun handlePoints(a: ArrayList<Char>, numbers: ArrayList<Double>): Int {
        foreach(a, '.') {
            if (isNum(a[it - 1]) && isNum(a[it + 1])) {
                val numberId = a.getNumberIdAfterSymbolId(it)
                if (!isInt(numbers[numberId - 1]) || !isInt(numbers[numberId])) {
                    LogUtils.e("!isInt(number[numberId - 1]) || !isInt(number[numberId])")
                    return ERROR
                }
                val pointSum = (a.indexWhile(it + 1) { isNum(it) } - it).toDouble()
                numbers[numberId - 1] +=
                        numbers[numberId] * Math.pow(10.0, -pointSum)
                numbers.removeAt(numberId)
                a.removeAt(it)
            } else {
                LogUtils.e("!isNum(a[it - 1]) && isNum(a[it + 1])")
                return ERROR
            }
        }
        return CORRECT
    }

    fun handleAddSubs(a: ArrayList<Char>, numbers: ArrayList<Double>) {
        foreachDown(a, '-') {
            if (it == 0 || !isNum(a[it - 1]) && a[it - 1] != ')' // e.g, (-
                    && a[it - 1] != ']') {
                numbers[a.getNumberIdAfterSymbolId(it)] *= -1.0
                a.removeAt(it)
            }
        }

        foreachDown(a, '+') {
            if (it == 0 || !isNum(a[it - 1]) && a[it - 1] != ')'
                    && a[it - 1] != ']') {
                a.removeAt(it)
            }
        }
    }

    fun calculateFunction(number: Double, functionId: Int): Double {
        var value = number
        when (functionId) {
            0 -> if (make == 0) {
                value = Math.sin(value * Math.PI / 180)
            } else {
                value = Math.sin(value)
            }
            1 -> if (make == 0) {
                value = Math.cos(value * Math.PI / 180)
            } else {
                value = Math.cos(value)
            }
            2 -> if (make == 0) {
                if (isInt(value) && (value - 90) % 180 == 0.0) {
                    throw InfiniteException()
                } else
                    value = Math
                            .tan(value * Math.PI / 180)
            } else {
                if (isInt((value - Math.PI / 2) / Math.PI)) {
                    throw InfiniteException()
                } else
                    value = Math
                            .tan(value)
            }
            3 -> if (make == 0) {
                if (isInt(value) && value % 180 == 0.0) {
                    throw InfiniteException()
                } else
                    value = 1 / Math
                            .tan(value * Math.PI / 180)
            } else {
                if (isInt(value / Math.PI)) {
                    throw InfiniteException()
                } else
                    value = 1 / Math
                            .tan(value)
            }
            4 -> value = Math.log10(value)
            5 -> value = Math.log(value)

            6 -> if (make == 0) {
                value = Math.asin(value) * 180 / Math.PI
            } else {
                value = Math.asin(value)
            }
            7 -> if (make == 0) {
                value = Math.acos(value) * 180 / Math.PI
            } else {
                value = Math.acos(value)
            }
            8 -> if (make == 0) {
                value = Math.atan(value) * 180 / Math.PI
            } else {
                value = Math.atan(value)
            }
            9 -> if (make == 0) {
                if (value == 0.0)
                    value = 90.0
                else
                    value = Math
                            .atan(1 / value) * 180 / Math.PI
            } else {
                if (value == 0.0)
                    value = Math.PI / 2
                else
                    value = Math
                            .atan(1 / value)
            }
            10 -> value = Math.abs(value)
            11 -> value = Math.floor(value)
        }
        return value
    }

    fun handleFunctions(a: ArrayList<Char>, numbers: ArrayList<Double>) {
        foreachDownFind(a, { a.isFunction(it) }) {
            symbolId, functionId ->
            val symbolEndId = symbolId + function[functionId].length
            if (symbolEndId < a.size) {
                if (isNum(a[symbolEndId])) {
                    val numberId = a.getNumberIdAfterSymbolId(symbolId)
                    numbers[numberId] = calculateFunction(numbers[numberId], functionId)
                    a.removeValue(symbolId, symbolEndId - 1)
                }
            }
        }
    }

    fun getDataList(a: ArrayList<Char>, numbers: ArrayList<Double>): ArrayList<Data>? {
        var leftSymbolId: Int
        var rightSymbolId: Int
        var leftNumberId: Int
        var rightNumberId: Int = -1

        val vector = arrayOfNulls<DoubleArray>(numbers.size)
        val dataList = ArrayList<Data>()

        foreach(a, '[') findLeftSymbol@ {
            var vectorSize = 0

            leftSymbolId = it

            leftNumberId = a.getNumberIdAfterSymbolId(leftSymbolId)

            (0..leftNumberId - 1)
                    .forEach {
                        dataList.add(Data(numbers[it]))
                    }

            rightNumberId = leftNumberId - 1

            foreachStart(a, it + 1) {
                if (a[it] == '[') {
                    return null
                } else if (a[it] == ';' || a[it] == ']') {
                    leftNumberId = rightNumberId + 1
                    rightNumberId = a.getNumberIdAfterSymbolId(it) - 1
                    vector[vectorSize] = DoubleArray(rightNumberId - leftNumberId + 1)

                    (0..rightNumberId - leftNumberId)
                            .forEach {
                                vector[vectorSize]!![it] = numbers[leftNumberId + it]
                            }
                    vectorSize++

                    if (a[it] == ']') {

                        if ((0..vectorSize - 2).any { vector[it]!!.size != vector[it + 1]!!.size }) {
                            return null
                        }

                        val newVector = Array(vectorSize) { DoubleArray(vector[0]!!.size) }
                        (0..vectorSize - 1)
                                .forEach {
                                    newVector[it] = vector[it]!!
                                }

                        dataList.add(Data(newVector))

                        rightSymbolId = it

                        leftNumberId = a.getNumberIdAfterSymbolId(leftSymbolId)
                        rightNumberId = a.getNumberIdAfterSymbolId(rightSymbolId) - 1

                        a[leftSymbolId] = '1'
                        a.removeValue(leftSymbolId + 1, rightSymbolId)

                        numbers.removeValue(leftNumberId + 1,
                                rightNumberId)

                        rightNumberId = leftNumberId
                        return@findLeftSymbol
                    }
                }
            }

            return null
        }

        foreachStart(numbers, rightNumberId + 1) {
            dataList.add(Data(numbers[it]))
        }
        return dataList
    }

    fun getNumbers(a: ArrayList<Char>): Value {

        amendFactorials(a)

        amendBrackets(a)

        amendIntervals(a)

        val numbers = getOriginalNumbers(a)

        if (handlePoints(a, numbers) == ERROR)
            return Value(ERROR)

        handleAddSubs(a, numbers)

        try {
            handleFunctions(a, numbers)
        } catch (e: InfiniteException) {
            return Value(INFINITE)
        }

        val dataList = getDataList(a, numbers) ?: return Value(ERROR)

        if (!checkErrorWord(a))
            return Value(ERROR)

        return Value(a, dataList)
    }

    fun checkErrorWord(a: ArrayList<Char>): Boolean {

        val builder = StringBuilder()
        for (char in a) builder.append(char)

        var str = builder.toString().trim()
        for (i in function.indices.reversed())
            str = str.replace(function[i], "1")

        val array = str.toCharArray()

        return array.indices.none { !isNum(array[it]) && !isCalcAllSymbol(array[it]) }
    }

    fun getAllSymbols(a: ArrayList<Char>): ArrayList<Char> {
        return (0..a.size - 1)
                .filter { isCalcAllSymbol(a[it]) }
                .map { a[it] } as ArrayList<Char>
    }

    fun getMainSymbols(a: ArrayList<Char>): ArrayList<Char> {
        return (0..a.size - 1)
                .filter { isCalcMainSymbol(a[it]) }
                .map { a[it] } as ArrayList<Char>
    }

    fun handleFunctionsWithBrackets(a: ArrayList<Char>, data: ArrayList<Data>): Value {

        foreachDownFind(a, { a.isFunction(it) }) {
            symbolId, functionId ->

            val symbolEndId = symbolId + function[functionId].length
            if (symbolEndId < a.size) {
                if (a[symbolEndId] == '(') {

                    val bracketsEndId: Int = a.findBracketsEndId(symbolEndId)
                    if (bracketsEndId == -1)
                        return Value(ERROR)

                    val subCharList = (symbolEndId + 1..bracketsEndId - 1).map { a[it] }

                    val numberStartId = a.getNumberIdAfterSymbolId(symbolId)
                    val numberEndId = a.getNumberIdAfterSymbolId(bracketsEndId) - 1

                    val subDataList = (numberStartId..numberEndId).map { data[it] }
                    val subDataListSize = subDataList.size
                    val v = calculateWithBrackets(subCharList as ArrayList<Char>, subDataList as ArrayList<Data>)
                    if (v.isCorrect != CORRECT || v.data[0].type == TYPE_MATRIX)
                        return Value(ERROR)

                    val result = v.data[0].number
                    val numberId = numberStartId
                    try {
                        data[numberId].number = calculateFunction(result, functionId)
                    } catch (e: InfiniteException) {
                        return Value(INFINITE)
                    }

                    data.removeValue(numberId + 1,
                            numberId + subDataListSize - 1)
                    a[symbolId] = '1'
                    a.removeValue(symbolId + 1,
                            bracketsEndId)
                }
            }
        }
        return Value(a, data)
    }

    fun calculateWithBrackets(a: ArrayList<Char>, data: ArrayList<Data>): Value {

        val allSymbols = getAllSymbols(a)
        val mainSymbols = getMainSymbols(allSymbols)

        if (data.size - 1 != mainSymbols.size) {
            return Value(ERROR)
        }

        var mainSymbolStartId: Int // 0
        var mainSymbolEndId: Int // 0

        var allSymbolStartId: Int // 0
        var allSymbolEndId: Int // 2

        // (1+2)*3+2*(1+2)*3
        foreachDown(allSymbols, '(') {

            allSymbolStartId = it

            val sum = (0..it)
                    .filter { allSymbols[it] == '(' || allSymbols[it] == ')' }
                    .count()

            mainSymbolStartId = allSymbolStartId - sum + 1

            allSymbolEndId = allSymbolStartId + (it..allSymbols.size - 1)
                    .takeWhile { allSymbols[it] != ')' }
                    .count()

            mainSymbolEndId = allSymbolEndId - sum - 1

            val subData = (mainSymbolStartId..mainSymbolEndId + 1).map { data[it] }
            val subSymbol = (mainSymbolStartId..mainSymbolEndId).map { mainSymbols[it] }
            val v = calculateWithoutBrackets(subSymbol as ArrayList<Char>, subData as ArrayList<Data>)

            if (v.isCorrect != CORRECT)
                return v
            data[mainSymbolStartId] = v.data[0]

            data.removeValue(mainSymbolStartId + 1,
                    mainSymbolEndId + 1)

            allSymbols.removeValue(allSymbolStartId,
                    allSymbolEndId)

            mainSymbols.removeValue(mainSymbolStartId,
                    mainSymbolEndId)
        }

        return calculateWithoutBrackets(mainSymbols, data)
    }

    fun calculateWithoutBrackets(symbols: ArrayList<Char>, data: ArrayList<Data>): Value {

        foreachCheck(symbols, '!') {
            if (data[it].type == TYPE_MATRIX || !isInt(data[it].number)) {
                LogUtils.e("Handle ! error")
                return Value(ERROR)
            }
            data[it].number = rank(data[it].number.toInt(),
                    data[it + 1].number.toInt()).toDouble()
            symbols.removeAt(it)
            data.removeAt(it + 1)
        }

        foreachCheck(symbols, '^') {

            if (data[it].type == TYPE_MATRIX || data[it + 1].type == TYPE_MATRIX) {
                LogUtils.e("Handle ^ error")
                return Value(ERROR)
            }
            data[it].number = Math.pow(data[it].number,
                    data[it + 1].number)
            symbols.removeAt(it)
            data.removeAt(it + 1)
        }

        foreachCheck(symbols, arrayOf('%', '÷')) {
            it, symbol ->

            when (symbol) {
                '%' -> {
                    if (data[it].type == TYPE_MATRIX || data[it + 1].type == TYPE_MATRIX) {
                        LogUtils.d("Handle % error")
                        return Value(ERROR)
                    }
                    data[it].number = data[it].number % data[it + 1].number
                    symbols.removeAt(it)
                    data.removeAt(it + 1)
                }
                '÷' -> {
                    if (data[it + 1].type == TYPE_MATRIX) {
                        LogUtils.e("Handle ÷ error")
                        return Value(ERROR)
                    }
                    if (data[it + 1].number == 0.0)
                        return Value(INFINITE)
                    if (data[it].type == TYPE_NUMBER)
                        data[it].number /= data[it + 1].number
                    else {
                        data[it].array!!.foreach {
                            array, i, j ->
                            array[i][j] /= data[it + 1].number
                        }
                    }
                    symbols.removeAt(it)
                    data.removeAt(it + 1)
                }
            }
        }

        foreachDown(symbols, '×') {
            if (data[it].type == TYPE_NUMBER && data[it + 1].type == TYPE_NUMBER) {
                data[it].number *= data[it + 1].number
            } else if (data[it].type == TYPE_MATRIX && data[it + 1].type == TYPE_NUMBER) {
                data[it].array!!.foreach {
                    array, i, j ->
                    array[i][j] *= data[it + 1].number
                }
            } else if (data[it].type == TYPE_NUMBER && data[it + 1].type == TYPE_MATRIX) {
                data[it + 1].array!!.foreach {
                    array, i, j ->
                    array[i][j] *= data[it].number
                }
                data[it] = data[it + 1]
            } else {
                if (data[it].array!![0].size != data[it + 1].array!!.size) {
                    return Value(ERROR)
                } else {
                    val new_array = Array(data[it].array!!.size) {
                        DoubleArray(data[it + 1].array!![0].size)
                    }
                    new_array.foreach {
                        array, i, j ->
                        array[i][j] = 0.0
                        for (k in 0..data[it + 1].array!!.size - 1) {
                            array[i][j] += data[it].array!![i][k] * data[it + 1].array!![k][j]
                        }
                    }
                    data[it].array = new_array
                }
            }
            symbols.removeAt(it)
            data.removeAt(it + 1)
        }

        foreachCheck(symbols, '&') {
            if (data[it].type == TYPE_MATRIX || data[it + 1].type == TYPE_MATRIX ||
                    !isInt(data[it].number) || !isInt(data[it + 1].number)) {
                LogUtils.e("Handle & error")
                return Value(ERROR)
            }
            data[it].number = (data[it].number.toInt() and data[it + 1].number.toInt()).toDouble()
            symbols.removeAt(it)
            data.removeAt(it + 1)
        }

        foreachCheck(symbols, '|') {
            if (data[it].type == TYPE_MATRIX || data[it + 1].type == TYPE_MATRIX ||
                    !isInt(data[it].number) || !isInt(data[it + 1].number)) {
                LogUtils.e("Handle | error")
                return Value(ERROR)
            }
            data[it].number = (data[it].number.toInt() or data[it + 1].number.toInt()).toDouble()
            symbols.removeAt(it)
            data.removeAt(it + 1)
        }

        foreach(symbols, '-') {
            if (data[it].type != data[it + 1].type)
                return Value(ERROR)
            if (data[it].type == TYPE_NUMBER && data[it + 1].type == TYPE_NUMBER) {
                data[it + 1].number = -data[it + 1].number
            } else {
                data[it + 1].array!!.foreach {
                    array, i, j ->
                    array[i][j] = -array[i][j]
                }
            }
            symbols[it] = '+'
        }

        foreachDown(symbols, '+') {
            if (data[it].type != data[it + 1].type)
                return Value(ERROR)
            if (data[it].type == TYPE_NUMBER && data[it + 1].type == TYPE_NUMBER) {
                data[it].number += data[it + 1].number
            } else {
                data[it].array!!.foreach {
                    array, i, j ->
                    array[i][j] += data[it + 1].array!![i][j]
                }
            }
        }

        return Value(symbols, data)
    }

    fun calculate(str: String): Value {

        val list: ArrayList<Char> = (0..str.length - 1).mapTo(ArrayList()) { str[it] }
        var value = getNumbers(list)
        if (value.isCorrect == CORRECT) {
            value = handleFunctionsWithBrackets(value.charList, value.data)
            if (value.isCorrect == ERROR)
                return value
            return calculateWithBrackets(value.charList, value.data)
        } else {
            return value
        }

    }

}