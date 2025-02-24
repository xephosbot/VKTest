package com.xbot.domain.model

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Failure<out T>(val data: T? = null, val error: Throwable) : Result<T>
}

inline fun <T> Result<T>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (T?, Throwable) -> Unit
) {
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Failure -> onFailure(data, error)
    }
}