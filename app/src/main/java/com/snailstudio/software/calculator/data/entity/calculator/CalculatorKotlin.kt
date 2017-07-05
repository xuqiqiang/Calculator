/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.function
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorUtils.isNum

/**
 * Created by xuqiqiang on 2016/04/17.
 */
fun <T> getSuccessiveSymbolSum(list: ArrayList<T>, startId: Int, symbol: Char)
        = (startId..list.size - 1)
        .takeWhile { list[it] == symbol }
        .count()

inline fun <T> replaceField(list: ArrayList<T>, startId: Int, end_id: Int, value: T) {
    list.removeValue(startId, end_id)
    list.add(startId, value)
}

inline fun <T> foreachStep(list: List<T>, action: (Int) -> Int) {
    var step: Int = 0
    (0..Int.MAX_VALUE)
            .forEach {
                if (it < list.size) {
                    if (it >= step)
                        step = it + action(it)
                } else {
                    return
                }
            }
}

inline fun <T> foreach(list: List<T>, action: (Int) -> Unit) {
    (0..Int.MAX_VALUE)
            .forEach {
                if (it < list.size) {
                    action(it)
                } else {
                    return
                }
            }
}

inline fun <T> foreachStart(list: List<T>, startId: Int, action: (Int) -> Unit) {
    (startId..Int.MAX_VALUE)
            .forEach {
                if (it < list.size) {
                    action(it)
                } else {
                    return
                }
            }
}

inline fun <T> foreach(list: List<T>, value: T, action: (Int) -> Unit) {
    (0..Int.MAX_VALUE)
            .forEach {
                if (it < list.size) {
                    if (list[it] == value)
                        action(it)
                } else {
                    return
                }
            }
}

inline fun <T> foreachCheck(list: List<T>, value: T, action: (Int) -> Unit) {

    var listId = 0
    while (listId < list.size) {
        if (list[listId] == value) {
            action(listId)
            listId--
        }
        listId++
    }
}

inline fun <T> foreachCheck(list: List<T>, values: Array<T>, action: (Int, T) -> Unit) {

    var listId = 0
    while (listId < list.size) {
        for (value in values) {
            if (list[listId] == value) {
                action(listId, value)
                listId--
                break
            }
        }
        listId++
    }
}

inline fun <T> foreachFind(list: List<T>, predicate: (Int) -> Int, action: (Int, Int) -> Unit) {
    (0..Int.MAX_VALUE)
            .forEach {
                if (it < list.size) {
                    val id = predicate(it)
                    if (id != -1)
                        action(it, id)
                } else {
                    return
                }
            }
}

inline fun <T> foreachDownFind(list: List<T>, predicate: (Int) -> Int, action: (Int, Int) -> Unit) {
    (list.size - 1 downTo 0)
            .forEach {
                val id = predicate(it)
                if (id != -1)
                    action(it, id)
            }
}

inline fun <T> foreachDown(list: List<T>, value: T, action: (Int) -> Unit) {
    (list.size - 1 downTo 0)
            .forEach {
                if (list[it] == value)
                    action(it)
            }
}

inline fun <T> ArrayList<T>.indexWhile(startId: Int, predicate: (T) -> Boolean): Int {
    var i = startId
    while (i < size && predicate(this[i])) {
        i++
    }
    return i - 1
}

inline fun ArrayList<Char>.getNumber(startId: Int): Double {
    var endInd = indexWhile(startId) { isNum(it) }
    return getNumber(startId, endInd)
}

inline fun ArrayList<Char>.getNumber(startId: Int, endInd: Int): Double {

    if (endInd < startId) {
        return 0.0
    }
    if (endInd == startId) {
        return (this[startId].toInt() - 48).toDouble()
    } else {
        var num: Double = 0.0
        for (i in startId..endInd) {
            num = num * 10 + (this[i].toInt() - 48).toDouble()
        }
        return num
    }
}

inline fun ArrayList<Char>.getNumberIdAfterSymbolId(symbolId: Int): Int {
    var i: Int = 0
    var id = 0
    while (i <= symbolId) {
        var step: Int = 1
        if (isNum(this[i])) {
            step = indexWhile(i) { isNum(it) } - i + 2
            id++
        }
        i += step
    }
    return id
}

inline fun ArrayList<Char>.findBracketsEndId(bracketsStartId: Int): Int {
    var leftSymbolSum: Int = 0
    var rightSymbolSum: Int = 0
    (bracketsStartId + 1..size - 1)
            .forEach findBracketsEnd@ {
                if (this[it] == '(')
                    leftSymbolSum++
                else if (this[it] == ')')
                    rightSymbolSum++
                if (leftSymbolSum + 1 == rightSymbolSum)
                    return it
            }
    return -1
}

inline fun String.findBracketsEndId(bracketsStartId: Int): Int {
    var leftSymbolSum: Int = 0
    var rightSymbolSum: Int = 0
    (bracketsStartId..length - 1)
            .forEach findBracketsEnd@ {
                if (this[it] == '(')
                    leftSymbolSum++
                else if (this[it] == ')')
                    rightSymbolSum++
                if (leftSymbolSum != 0 && leftSymbolSum == rightSymbolSum)
                    return it
            }
    return -1
}

inline fun ArrayList<Char>.isFunction(id: Int): Int {
    (function.size - 1 downTo 0)
            .forEach outside@ {
                (id..id + function[it].length - 1)
                        .forEach {
                            listId ->
                            if (listId >= this.size
                                    || this[listId] != function[it][listId - id])
                                return@outside
                        }
                return it
            }
    return -1
}

inline fun <T> ArrayList<T>.removeValue(start_id: Int, end_id: Int) {
    for (i in start_id..end_id) {
        removeAt(start_id)
    }
}

inline fun Array<DoubleArray>.foreach(action: (Array<DoubleArray>, Int, Int) -> Unit) {
    for (i in 0..this.size - 1)
        for (j in 0..this[i].size - 1) {
            action(this, i, j)
        }
}
