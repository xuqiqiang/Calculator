package com.snailstudio.software.calculator.data.exception

import com.snailstudio.software.calculator.domain.exception.ErrorBundle

/**
 * Wrapper around Exceptions used to manage errors in the repository.
 *
 * Created by xuqiqiang on 2016/04/17.
 */
class RepositoryErrorBundle(private val exception: Exception?) : ErrorBundle {

    override fun getException(): Exception? {
        return exception
    }

    override fun getErrorMessage(): String {
        var message = ""
        if (this.exception != null) {
            message = this.exception.message!!
        }
        return message
    }
}
