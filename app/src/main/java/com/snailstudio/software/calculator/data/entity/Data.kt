/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class Data {
    var type: Int = 0
    var number: Double = 0.toDouble()
    var array: Array<DoubleArray>? = null

    companion object {
        val TYPE_NUMBER = 0
        val TYPE_MATRIX = 1
    }

    constructor(number: Double) {
        this.number = number
        type = TYPE_NUMBER
    }

    constructor(array: Array<DoubleArray>) {
        this.array = array
        type = TYPE_MATRIX
    }
}