package com.example.igeniusandroidtest.utils

suspend fun <T : Any> NetworkResult<T>.onSuccess(executable: suspend (T) -> Unit): NetworkResult<T> =
    apply {
        if (this is NetworkResult.Success<T>) {
            executable(data)
        }
    }

suspend fun <T : Any> NetworkResult<T>.onLoading(executable: suspend () -> Unit): NetworkResult<T> =
    apply {
        if (this is NetworkResult.Loading<T>) {
            executable()
        }
    }

suspend fun <T : Any> NetworkResult<T>.onError(executable: suspend (code: Int, message: String?) -> Unit): NetworkResult<T> =
    apply {
        if (this is NetworkResult.Error<T>) {
            executable(code, message)
        }
    }

suspend fun <T : Any> NetworkResult<T>.onException(executable: suspend (e: Throwable) -> Unit): NetworkResult<T> =
    apply {
        if (this is NetworkResult.Exception<T>) {
            executable(e)
        }
    }
