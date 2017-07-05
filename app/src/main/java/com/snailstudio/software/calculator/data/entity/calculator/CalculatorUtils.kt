/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

/**
 * Created by xuqiqiang on 2016/04/17.
 */
object CalculatorUtils {

    var function = arrayOf("sin", "cos", "tan", "cot", "log", "ln", "asin", "acos", "atan", "acot",
            "abs", "floor")

    val letters = arrayOf("a", "b", "c", "d", "e")

    fun insertString(d: String, id: Int, p: String): String {

        if (d.isNullOrEmpty())
            return p
        val s1: String
        val s2: String
        if (id !in 0..d.length) {
            android.util.Log.e("CalculatorUtils", "id !in 0..d.length")
            return d
        }
        if (id == 0) {
            return p + d
        } else if (id == d.length) {
            return d + p
        } else {
            s1 = d.substring(0, id)
            s2 = d.substring(id, d.length)
            return s1 + p + s2
        }
    }

    fun removeString(d: String, id: Int): String {
        if (d.isNullOrEmpty())
            return d
        val s1: String
        val s2: String
        val len = d.length
        if (id !in 0..len - 1) {
            android.util.Log.e("CalculatorUtils", "id !in 0..len - 1")
            return d
        }
        if (id == 0) {
            s1 = d.substring(1, len)
            return s1
        } else if (id == len - 1) {
            return d.substring(0, len - 1)
        } else {
            s1 = d.substring(0, id)
            s2 = d.substring(id + 1, len)

            return s1 + s2
        }
    }

    fun removeString(d: String, start_id: Int, end_id: Int): String {
        if (d.isNullOrEmpty())
            return d
        val s1: String
        val s2: String
        val len = d.length
        if (start_id !in 0..len - 1 || end_id !in 0..len - 1) {
            android.util.Log.e("CalculatorUtils", "start_id !in 0..len - 1 || end_id !in 0..len - 1")
            return d
        }
        if (start_id == 0 && end_id != len - 1) {
            s1 = d.substring(end_id + 1, len)
            return s1
        } else if (start_id != 0 && end_id == len - 1) {
            return d.substring(0, start_id)
        } else if (start_id == 0 && end_id == len - 1) {
            return ""
        } else {
            s1 = d.substring(0, start_id)
            s2 = d.substring(end_id + 1, len)

            return s1 + s2
        }
    }

    fun getSum(str: String, c: Char) = (0..str.length - 1).count { str[it] == c }

    fun isInt(a: Double) = a == Math.floor(a)

    fun isNumPoint(c: Char) = c in '0'..'9' || c == '.'

    fun isNumWordPoint(c: Char) = c == '.' || c in '0'..'9' || c in 'a'..'z' || c in 'A'..'Z'

    fun removeSpace(str: String?): String {
        if (str.isNullOrEmpty())
            return str!!
        var start: Int = 0
        var end: Int = str!!.length - 1
        while (start < str.length && str[start] == ' ') {
            start++
        }
        while (end >= 0 && str[end] == ' ') {
            end--
        }
        if (start <= end)
            return str.substring(start, end + 1)
        else
            return ""
    }

    val calcSymbol = arrayOf('.', '+', '-', '×', '÷', '^', '%', '!', '&', '|')

    fun isCalcSymbol(c: Char) = c in calcSymbol

    val calcMainSymbol = arrayOf('+', '-', '×', '÷', '^', '%', '!', '&', '|')

    fun isCalcMainSymbol(c: Char) = c in calcMainSymbol

    val calcAllSymbol = arrayOf('+', '-', '×', '÷', '^', '%', '!', '&', '|', '(', ')')

    fun isCalcAllSymbol(c: Char) = c in calcAllSymbol

    val constants = arrayOf('π', 'E', 'φ', 'R')

    fun isConstant(c: Char) = c in constants

    fun getConstant(c: Char): Double {
        if (c == 'π')
            return Math.PI
        else if (c == 'E')
            return Math.E
        else if (c == 'φ')
            return 0.6180339887498948
        else if (c == 'R')
            return Math.random()
        else
            return 0.0
    }

    fun equals(a: Float, b: Float) = a <= b + 0.000001f && a >= b - 0.000001f

    fun equals(a: Double, b: Double) = a <= b + 0.000001f && a >= b - 0.000001f

    fun isNum(c: Char) = c in '0'..'9'

    fun rank(n: Int, rate: Int): Int {
        var i: Int = n
        var sum = 1
        while (i > 0) {
            sum *= i
            i -= rate
        }
        return sum
    }
}
