package com.snailstudio.software.calculator.domain.exception

/**
 * Interface to represent a wrapper around an [Exception] to manage errors.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
interface ErrorBundle {
    fun getException(): Exception?

    fun getErrorMessage(): String
}
