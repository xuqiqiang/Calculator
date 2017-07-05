package com.snailstudio.software.calculator.data.mapper

import com.snailstudio.software.calculator.data.entity.Value
import com.snailstudio.software.calculator.data.entity.Value.Companion.CORRECT
import com.snailstudio.software.calculator.data.entity.Value.Companion.ERROR
import com.snailstudio.software.calculator.data.entity.Value.Companion.INFINITE
import com.snailstudio.software.calculator.data.entity.calculator.CalculatorManager
import com.snailstudio.software.calculator.data.entity.calculator.foreach
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mapper class used to transform [Value] (in the data layer) to [String] in the
 * domain layer.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
@Singleton
class CalculatorDataMapper
@Inject
internal constructor(val cm: CalculatorManager) {

    /**
     * Transform a [Value] into an [String].

     * @param value Object to be transformed.
     * *
     * @return [String] if valid [Value] otherwise null.
     */
    fun transform2Result(value: Value): String? {
        var result: String? = null
        when (value.isCorrect) {
            INFINITE -> {
                result = "∞"
            }
            ERROR -> {
                result = "输入有误"
            }
            CORRECT -> {
                val data = value.data[0]
                if (data.type == 0) {
                    result = cm.double2String(data.number)
                    cm.saveToClipboard(result)
                } else {
                    val builder = StringBuilder()
                    data.array!!.foreach {
                        array, i, j ->
                        if (j == 0 && i > 0) builder.deleteCharAt(builder.length - 1).append("\n ")
                        builder.append(cm.double2String(array[i][j]) + " ")
                    }
                    result = builder.deleteCharAt(builder.length - 1).toString()
                    cm.saveToClipboard(result)
                }
            }
        }
        return result
    }

    /**
     * Transform a [Value] into an [String].

     * @param value Object to be transformed.
     * *
     * @return [String] if valid [Value] otherwise null.
     */
    fun transform2Input(value: Value): String? {
        var result: String? = null
        when (value.isCorrect) {
            INFINITE -> {
                result = "∞"
            }
            ERROR -> {
                result = "输入有误"
            }
            CORRECT -> {
                val data = value.data[0]
                if (data.type == 0) {
                    result = cm.double2String(data.number)
                } else {
                    val builder = StringBuilder("[")
                    data.array!!.foreach {
                        array, i, j ->
                        if (j == 0 && i > 0) builder.deleteCharAt(builder.length - 1).append(";")
                        builder.append(cm.double2String(array[i][j]) + ",")
                    }
                    result = builder.deleteCharAt(builder.length - 1).append("]").toString()
                }
            }
        }
        return result
    }
}
