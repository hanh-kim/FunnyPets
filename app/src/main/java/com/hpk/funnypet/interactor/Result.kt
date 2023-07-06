package com.hpk.funnypet.interactor

sealed class Result<out Failure: Throwable, out Success> {
    data class Error<out Failure : Throwable>(val error: Failure) : Result<Failure, Nothing>()
    data class Success<out Success>(val data: Success) : Result<Nothing, Success>()

    val isFailure: Boolean
        get() = when (this) {
            is Error -> {
                true
            }
            is Result.Success -> {
                false
            }
            else -> {
                false
            }
        }
    val responseData: Success?
        get() = when (this) {
            is Result.Success -> {
                this.data
            }
            else -> {
                null
            }
        }

    /**
     * @see onFailure
     * @see onSuccess
     */
    inline fun <R> fold(onFailure: (Failure) -> R, onSuccess: (Success) -> R): R =
        when (this) {
            is Error -> onFailure(error)
            is Result.Success -> onSuccess(data)
        }
}