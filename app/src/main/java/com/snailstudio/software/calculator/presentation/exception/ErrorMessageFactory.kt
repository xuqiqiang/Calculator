/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 *
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.software.calculator.presentation.exception

import android.content.Context
import com.snailstudio.software.calculator.R
import com.snailstudio.software.calculator.data.exception.*

/**
 * Factory used to create error messages from an Exception as a condition.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
object ErrorMessageFactory {

    /**
     * Creates a String representing an error message.

     * @param context   Context needed to retrieve string resources.
     * *
     * @param exception An exception used as a condition to retrieve the correct error message.
     * *
     * @return [String] an error message.
     */
    fun create(context: Context, exception: Exception?): String {
        var message = context.getString(R.string.exception_unknown)
        if (exception is NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection)
        } else if (exception is InputErrorException) {
            message = context.getString(R.string.exception_input_error)
            val detail = exception.message
            if(!detail.isNullOrEmpty())
                message += ":" + detail
        } else if (exception is InfiniteException) {
            message = context.getString(R.string.exception_input_infinite)
        } else if (exception is SymbolContentEmptyException) {
            message = context.getString(R.string.exception_symbol_content_empty)
        } else if (exception is SymbolNameEmptyException) {
            message = context.getString(R.string.exception_symbol_name_empty)
        } else if (exception is SymbolNameExistException) {
            message = context.getString(R.string.exception_symbol_name_exist)
        } else if (exception is SymbolNameHeadException) {
            message = context.getString(R.string.exception_symbol_name_head)
        } else if (exception is SymbolNameStructureException) {
            message = context.getString(R.string.exception_symbol_name_structure)
        } else if (exception is SymbolParameterErrorException) {
            message = context.getString(R.string.exception_symbol_parameter)
        }

        return message
    }
}