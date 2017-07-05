package com.snailstudio.software.calculator.data.exception

/**
 * Created by xuqiqiang on 2016/04/17.
 */
class InputErrorException : Exception {
    constructor() : super("")

    //constructor(context: Context) : super(context.getString(R.string.input_error))

    constructor(cause: Throwable) : super(cause)

    constructor(message: String) : super(message)
}
