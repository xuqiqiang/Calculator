/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.repository.datasource

import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.library.utils.Cache
import com.snailstudio.library.utils.Cache.*
import com.snailstudio.software.calculator.data.entity.Value
import com.snailstudio.software.calculator.data.entity.calculator.Calculator
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isNumWordPoint
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.letters
import com.snailstudio.software.calculator.data.entity.calculator.Distinguisher
import com.snailstudio.software.calculator.data.exception.*
import com.snailstudio.software.calculator.domain.interactor.DeleteSymbol
import com.snailstudio.software.calculator.domain.interactor.SaveReport
import com.snailstudio.software.calculator.domain.interactor.SaveSymbol
import com.snailstudio.software.calculator.presentation.model.Symbol
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.SYMBOL_TYPE_FUNCTION
import com.snailstudio.software.calculator.presentation.view.activity.SymbolActivity.Companion.SYMBOL_TYPE_VALUE
import io.reactivex.Observable
import javax.inject.Inject

/**
 * [CalculatorDataStore] implementation based on connections to the api (Local).
 *
 * Created by xuqiqiang on 2016/04/17.
 */
internal class CalculatorDataStoreImpl
@Inject
internal constructor(private val distinguisher: Distinguisher,
                     private val calculator: Calculator
) : CalculatorDataStore {

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    override fun distinguish(input: String): Observable<String> {
        return Observable.create<String> { emitter ->
            try {
                val result = distinguisher.transInput(input)
                emitter.onNext(result)
                emitter.onComplete()
            } catch (e: Exception) {
                LogUtils.e(e, "distinguish")
                emitter.onError(e)
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    override fun calculate(input: String): Observable<Value> {
        return Observable.create<Value> { emitter ->
            try {
                val result = calculator.calculate(input)
                LogUtils.`object`(result)
                emitter.onNext(result)
                emitter.onComplete()
            } catch (e: Exception) {
                LogUtils.e(e, "calculate")
                emitter.onError(InputErrorException())
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun getSymbols(): Observable<List<Symbol>> {

        return Observable.create<List<Symbol>> { emitter ->
            try {

                val names: Array<String> = arrayOf("E", "π",

                        "φ", "R", "!", "%",

                        "^", "&", "|",

                        "sin", "cos", "tan", "cot",

                        "asin", "acos", "atan", "acot", "log", "ln",

                        "abs", "floor",

                        "[", "]", ";"
                )

                val helps: Array<String> = arrayOf("E，自然常数，值为2.718281828459045",
                        "π，圆周率，值为3.141592653589793",
                        "φ，黄金分割率，值为0.6180339887498948",
                        "Random，产生0和1之间的随机小数",
                        "！，表示阶乘，连续n个！表示n阶乘，例如4！=24,4！！=8",
                        "%，求余数，例如6%4=2",

                        "^，乘方，例如3^2=9,9^0.5=3",

                        "&，按位与",
                        "|，按位或",

                        "sin，正弦函数",
                        "cos，余弦函数",
                        "tan，正切函数", "cot，余切函数",
                        "asin，反正弦函数",
                        "acos，反余弦函数",
                        "atan，反正切函数",
                        "acot，反余切函数",
                        "log，以10为底的对数",
                        "ln，以e为底的对数",

                        "abs，绝对值",
                        "floor，返回不大于的最大整数",

                        "[和]，中括号，表示矩阵，例如：[1,2,3;7,8,9]",
                        "[和]，中括号，表示矩阵，例如：[1,2,3;7,8,9]",
                        ";，分号，用来隔开矩阵的行")

                val list: ArrayList<Symbol> = ArrayList()

                (0..names.size - 1).mapTo(list) { Symbol(-1, SYMBOL_TYPE_VALUE, names[it], names[it], helps[it], true) }

                var i: Int = 0

                while (i < readInt("new_value_sum", 0)) {

                    val name = readString("new_value_" + i + "_name", "???")
                    val content = readString("new_value_" + i
                            + "_content", "???")
                    list.add(Symbol(i, SYMBOL_TYPE_VALUE, name, name, name + "=" + content, false))

                    i++
                }

                i = 0
                while (i < readInt("new_function_sum", 0)) {
                    val name = readString("new_function_" + i + "_name", "???")


                    val num = readInt("new_function_"
                            + i + "_num", 0)
                    val content = readString("new_function_"
                            + i + "_content", "???")

                    var help = name + "("
                    var j: Int = 0
                    while (j < num) {
                        help += letters[j] + ","
                        j++
                    }
                    help = help.substring(0, help.length - 1)
                    help += ")"

                    help += "=" + content
                    list.add(Symbol(i, SYMBOL_TYPE_FUNCTION, name, name + "(", help, false))

                    i++
                }

                emitter.onNext(list)
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    fun getCurrentSymbolName(type: Int?, id: Int?): List<String> {
        val currentName = ArrayList<String>()

        val current = arrayOf("e", "sin", "cos", "tan", "log", "ln")
        (0..current.size - 1).mapTo(currentName) { current[it] }

        (0..readInt("new_value_sum", 0) - 1)
                .filterNot { type == SYMBOL_TYPE_VALUE && it == id }
                .mapTo(currentName) { readString("new_value_" + it + "_name", "???") }

        (0..readInt("new_function_sum", 0) - 1)
                .filterNot { type == SYMBOL_TYPE_FUNCTION && it == id }
                .mapTo(currentName) {
                    readString("new_function_" + it + "_name",
                            "???")
                }
        return currentName
    }

    fun checkName(name: String?, type: Int?, id: Int?): Exception? {
        if (name.isNullOrEmpty()) {
            return SymbolNameEmptyException()
        } else if (!(name!![0] in 'a'..'z' || name[0] in 'A'..'Z')) {
            return SymbolNameHeadException()
        }
        (1..name.length - 1)
                .filterNot {
                    name[it] in 'a'..'z'
                            || name[it] in 'A'..'Z' || name[it] in '0'..'9'
                }
                .forEach { return SymbolNameStructureException() }

        val currentSymbolName = getCurrentSymbolName(type, id)
        (0..currentSymbolName.size - 1)
                .filter { name == currentSymbolName[it] }
                .forEach { return SymbolNameExistException() }
        return null
    }

    fun checkInput(content: String?, type: Int?, parameterNumber: Int?): Value {

        if (content.isNullOrEmpty()) {
            throw SymbolContentEmptyException()
        }

        var testInput = content

        if (type == SYMBOL_TYPE_FUNCTION) {
            var i: Int = parameterNumber!!

            while (i < letters.size) {
                var id = testInput!!.indexOf(letters[i])
                while (id != -1) {
                    if (!(id > 0 && isNumWordPoint(testInput[id - 1]) || id < testInput
                            .length - 1 && isNumWordPoint(testInput[id + 1])))
                        throw SymbolParameterErrorException()
                    id = testInput.indexOf(letters[i], id + 1)
                }
                i++
            }
            i = 0
            while (i < parameterNumber) {
                var id = testInput!!.indexOf(letters[i])
                while (id != -1) {
                    if (!(id > 0 && isNumWordPoint(testInput!![id - 1]) || id < testInput!!
                            .length - 1 && isNumWordPoint(testInput[id + 1])))
                        testInput = testInput.substring(0, id) + "(2)" + testInput.substring(id + 1,
                                testInput.length)
                    id = testInput.indexOf(letters[i], id + 1)
                }
                i++
            }
        }
        try {
            val value = calculator.calculate(
                    distinguisher.transInput(testInput!!.trim())
                            .trim())
            when (value.isCorrect) {
                Value.INFINITE -> {
                    throw InfiniteException()
                }
                Value.ERROR -> {
                    throw InputErrorException()
                }
            }
            return value
        } catch (e: Exception) {
            if (e is InfiniteException)
                throw e
            else
                throw InputErrorException()
        }
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun saveSymbol(params: SaveSymbol.Params, result: String): String {
        if (params.type == SYMBOL_TYPE_VALUE) {
            if (params.id == -1) {
                var new_value_sum = readInt("new_value_sum", 0)

                Cache.writeString("new_value_" + new_value_sum + "_name",
                        params.name!!.trim())
                Cache.writeString("new_value_" + new_value_sum + "_content",
                        params.content!!.trim())
                Cache.writeString("new_value_" + new_value_sum + "_result",
                        result)

                new_value_sum++
                Cache.writeInt("new_value_sum", new_value_sum)

            } else {
                Cache.writeString("new_value_" + params.id + "_name", params.name!!.trim())
                Cache.writeString("new_value_" + params.id + "_content",
                        params.content!!.trim())
                Cache.writeString("new_value_" + params.id + "_result",
                        result)
            }
        } else {
            if (params.id == -1) {
                var new_function_sum = readInt("new_function_sum", 0)

                Cache.writeString("new_function_" + new_function_sum + "_name",
                        params.name!!.trim())
                Cache.writeInt("new_function_" + new_function_sum + "_num",
                        params.parameterNumber!!)
                Cache.writeString(
                        "new_function_" + new_function_sum + "_content",
                        params.content!!.trim())

                new_function_sum++
                Cache.writeInt("new_function_sum", new_function_sum)

            } else {
                Cache.writeString("new_function_" + params.id + "_name", params.name!!.trim())
                Cache.writeInt("new_function_" + params.id + "_num", params.parameterNumber!!)
                Cache.writeString("new_function_" + params.id + "_content",
                        params.content!!.trim())
            }
        }

        var str = params.name
        if (params.type == SYMBOL_TYPE_FUNCTION) {
            str += "("
            var i: Int = 0
            while (i < params.parameterNumber!!) {
                str += letters[i] + ","
                i++
            }
            str = str!!.substring(0, str.length - 1)
            str += ")"
        }
        str += "=" + params.content
        return str
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun checkSymbol(params: SaveSymbol.Params?): Observable<Value> {

        return Observable.create<Value> { emitter ->
            try {
                var exception = checkName(params!!.name, params.type, params.id)
                if (exception != null) {
                    emitter.onError(exception)
                    return@create
                }
                emitter.onNext(checkInput(params.content, params.type, params.parameterNumber))
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.e(e, "checkSymbol error")
                emitter.onError(e)
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun checkSymbol(name: String?,
                             content: String?,
                             type: Int?,
                             id: Int?,
                             parameterNumber: Int?): Observable<String> {

        return Observable.create<String> { emitter ->
            try {
                var exception = checkName(name, type, id)
                if (exception != null) {
                    emitter.onError(exception)
                } else {
//                    exception = checkInput(content, type, parameterNumber)
//                    if (exception != null) {
//                        emitter.onError(exception)
//                    } else {
//                        emitter.onNext("Succeed")
//                        emitter.onComplete()
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [List<Symbol>].
     *
     * @param Void .
     */
    override fun deleteSymbol(params: DeleteSymbol.Params): Observable<String> {

        return Observable.create<String> { emitter ->
            try {
                val symbol = params.symbol
                var new_value_sum = readInt("new_value_sum", 0)

                var new_function_sum = readInt("new_function_sum", 0)

                var i: Int

                var new_name: String
                var new_num: Int
                var new_content: String
                var new_result: String

                if (symbol!!.type == SYMBOL_TYPE_VALUE) {
                    new_value_sum--

                    i = symbol.id
                    while (i < new_value_sum) {
                        new_name = readString("new_value_" + (i + 1) + "_name",
                                "???")
                        new_content = readString("new_value_" + (i + 1)
                                + "_content", "???")
                        new_result = readString("new_value_" + (i + 1)
                                + "_result", "???")

                        writeString("new_value_" + i + "_name", new_name)
                        writeString("new_value_" + i + "_content", new_content)
                        writeString("new_value_" + i + "_result", new_result)
                        i++
                    }

                    writeInt("new_value_sum", new_value_sum)
                } else {
                    new_function_sum--

                    i = symbol.id
                    while (i < new_function_sum) {

                        new_name = readString("new_function_" + (i + 1) + "_name",
                                "???")
                        new_num = readInt("new_function_" + (i + 1) + "_num", 0)
                        new_content = readString("new_function_" + (i + 1)
                                + "_content", "???")

                        writeString("new_function_" + i + "_name", new_name)
                        writeInt("new_function_" + i + "_num", new_num)
                        writeString("new_function_" + i + "_content", new_content)
                        i++

                    }

                    writeInt("new_function_sum", new_function_sum)
                }

                emitter.onNext(symbol.name)
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param input, input.
     */
    override fun saveReport(params: SaveReport.Params): Observable<String> {
        return Observable.create<String> { emitter ->
            try {
                var history = readInt("history", 0)
                writeString("history" + history, params.input + params.result)
                LogUtils.d(params.input + params.result)
                history++
                writeInt("history", history)
                emitter.onNext("history" + history)
                emitter.onComplete()
            } catch (e: Exception) {
                LogUtils.e(e, "saveReport")
                emitter.onError(e)
            }
        }
    }

    /**
     * Get an [Observable] which will emit a [String].
     *
     * @param Void .
     */
    override fun getReports(): Observable<String> {

        return Observable.create<String> { emitter ->
            try {

                var history = readInt("history", 0)

                var i: Int = 0
                var content = ""

                while (i < history) {
                    content += readString("history" + i, "") + '\n'
                    i++
                }
                emitter.onNext(content)
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}
