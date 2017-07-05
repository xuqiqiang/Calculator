package com.snailstudio.software.calculator.domain.exception

/**
 * Wrapper around Exceptions used to manage default errors.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class DefaultErrorBundle(private val exception: Exception?) : ErrorBundle {

    override fun getException(): Exception? = exception

    override fun getErrorMessage()
            = if (exception != null) this.exception.message!! else DEFAULT_ERROR_MSG

    companion object {
        private val DEFAULT_ERROR_MSG = "Unknown error"
    }
}
