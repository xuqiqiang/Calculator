/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

import com.snailstudio.library.logutils.LogUtils
import com.snailstudio.library.utils.Cache.readInt
import com.snailstudio.library.utils.Cache.readString
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.getSum
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.insertString
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isNumWordPoint
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.letters
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.removeString
import com.snailstudio.software.calculator.data.exception.InputErrorException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class Distinguisher
@Inject
internal constructor() {

    fun transInput(str: String) = amendValues(amendBrackets(str.trim()))

    fun amendBrackets(str: String): String {
        var str = str
        val leftSum = (0..str.length - 1).count { str[it] == '(' }
        val rightSum = (0..str.length - 1).count { str[it] == ')' }
        if (leftSum > rightSum) {
            for (i in rightSum..leftSum - 1) {
                str += ")"
            }
        } else if (leftSum < rightSum) {
            for (i in leftSum..rightSum - 1) {
                str = "(" + str
            }
        }
        return str
    }

    fun amendValues(result: String): String {
        var result = result
        val newValueSum = readInt("new_value_sum", 0)
        val newFunctionSum = readInt("new_function_sum", 0)
        do {
            var find = false
            for (i in 0..newValueSum - 1) {
                val valueName = readString("new_value_" + i + "_name", "???")
                val valueResult = readString("new_value_" + i + "_result", "???")
                if (valueName == "???" || valueResult == "???") {
                    throw InputErrorException()
                } else {
                    val newResult = changeValues(valueName, valueResult, result)
                    if (result != newResult) {
                        result = newResult
                        LogUtils.d("result:" + result)
                        find = true
                    }
                }
            }

            for (i in 0..newFunctionSum - 1) {
                val name = readString("new_function_" + i + "_name", "???")
                val sum = readInt("new_function_" + i + "_num", 0)
                val content = readString("new_function_" + i + "_content",
                        "???")
                if (name == "???" || sum == 0 || content == "???") {
                    throw InputErrorException()
                } else {
                    val newResult = changeFunctions(name, sum, content, result)
                    if (result != newResult) {
                        result = newResult
                        LogUtils.d("result:" + result)
                        find = true
                    }
                }
            }
        } while (find)
        return result
    }

    fun changeValues(valueName: String, valueResult: String, str: String): String {
        var str = str
        var startId: Int
        var endId: Int
        startId = str.indexOf(valueName)
        while (startId != -1) {
            LogUtils.d("amendValues:" + str)
            endId = startId + valueName.length - 1
            if (!(startId > 0 && isNumWordPoint(str[startId - 1]) || endId < str
                    .length - 1 && isNumWordPoint(str[endId + 1])))
                str = str.substring(0, startId) + '(' + valueResult + ')' + str.substring(endId + 1, str.length)
            startId = str.indexOf(valueName, endId + 1)
        }
        return str
    }

    fun changeFunctions(name: String, sum: Int, content: String,
                        str: String): String {
        var str = str
        var startId: Int
        var endId = -1
        while (true) {
            LogUtils.d("changeFunctions")
            startId = str.indexOf(name + "(", endId + 1)
            if (startId == -1)
                return str
            else {
                if (startId > 0 && isNumWordPoint(str[startId - 1])) {
                    endId = startId
                } else {
                    endId = str.indexOf(")", startId)
                    if (endId == -1) {
                        throw InputErrorException("右括号不够")
                    } else {
                        endId = str.findBracketsEndId(startId)
                        if (startId + name.length + 1 >= endId) {
                            throw InputErrorException("函数" + name + "参数为空")
                        }

                        val parameter = arrayOfNulls<String>(sum)
                        val parameterString = str.substring(
                                startId + name.length + 1, endId)

                        val map = inter(parameterString)
                        val symbolSum = getInterSum(parameterString, ',',
                                map)

                        if (sum != symbolSum + 1) {
                            throw InputErrorException("函数" + name + "参数数量有误")
                        } else {
                            var p = 0
                            var parameterStartId = 0
                            for (i in 0..parameterString.length - 1) {
                                if (parameterString[i] == ',' && map[i] == 1) {
                                    if (parameterStartId >= i) {
                                        throw InputErrorException(
                                                "函数" + name
                                                        + "第" + (p + 1)
                                                        + "个参数" + "为空")
                                    }
                                    parameter[p] = parameterString.substring(
                                            parameterStartId, i)
                                    p++
                                    parameterStartId = i + 1
                                }
                            }
                            parameter[p] = parameterString.substring(parameterStartId,
                                    parameterString.length)
                            var result = content
                            for (i in 0..sum - 1) {
                                result = changeValues(letters[i], parameter[i]!!, result)
                            }

                            str = removeString(str, startId, endId)
                            str = insertString(str, startId, "(" + result
                                    + ")")
                        }
                    }
                }
            }
        }
    }

    fun getInterSum(str: String, c: Char, map: IntArray)
            = (0..str.length - 1).count { str[it] == c && map[it] == 1 }

    fun inter(str: String): IntArray {
        val len = str.length
        val map = IntArray(len)
        if (str[0] == '(' || str[0] == ')')
            map[0] = 0
        else
            map[0] = 1
        for (i in 1..len - 1) {
            if (getSum(str.substring(0, i), '(') == getSum(
                    str.substring(0, i), ')')
                    && str[i] != '(' && str[i] != ')') {
                map[i] = 1
            } else
                map[i] = 0
        }
        return map
    }
}