/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.data.entity.calculator

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.autoSave
import com.snailstudio.software.calculator.data.cache.SharedPreferencesData.pointSum
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class CalculatorManager
@Inject
internal constructor(val context: Context) {

    var format: java.text.NumberFormat = java.text.NumberFormat.getInstance()
    val clipboard = context
            .getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    fun double2String(d: Double): String {
        format.maximumFractionDigits = pointSum
        var result = format.format(d)
        result = remove(result, ',')
        return result
    }

    fun saveToClipboard(result: String) {
        if (autoSave) {
            clipboard.text = result
        }
    }

    fun remove(d: String, c: Char): String {
        var d = d
        var s1: String
        var s2: String
        var id = d.indices.indexOfFirst({ it -> d[it] == c })
        while (id != -1) {
            s1 = d.substring(0, id)
            s2 = d.substring(id + 1, d.length)
            d = s1 + s2
            id = d.indices.indexOfFirst({ it -> d[it] == c })
        }
        return d
    }
}