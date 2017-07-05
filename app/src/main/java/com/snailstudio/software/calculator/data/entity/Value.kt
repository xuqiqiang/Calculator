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
class Value {
    lateinit var charList: ArrayList<Char>
    lateinit var data: ArrayList<Data>
    var isCorrect: Int = 0

    companion object {
        val ERROR = 0
        val CORRECT = 1
        val INFINITE = 2
    }

    /**
     * Description :构造函数,
     * @param a :字符数组
     * *
     * @param data :字符串中提取出来的数
     */

    constructor(a: java.util.ArrayList<Char>, data: java.util.ArrayList<Data>) {
        this.charList = a
        this.data = data
        this.isCorrect = CORRECT
    }


    /**
     * Description :构造函数,
     * @param isCorrect :
     * * 0:输入不正确;
     * * 1:正确;
     * * 2:结果为无穷大
     */
    constructor(isCorrect: Int) {
        this.isCorrect = isCorrect
    }
}