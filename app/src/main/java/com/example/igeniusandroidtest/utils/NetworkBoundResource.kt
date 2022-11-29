package com.example.igeniusandroidtest.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

inline fun <ResultType : Any, RequestType : Any> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<NetworkResult<ResultType>> = channelFlow {
    val data = query().first()
    if (shouldFetch(data)) {
        val loading = launch { query().collect { send(NetworkResult.Loading()) } }
        try {
            delay(2000)
            saveFetchResult(fetch())
            loading.cancel()
            query().collect { send(NetworkResult.Success(it)) }
        } catch (t: Throwable) {
            loading.cancel()
            query().collect { send(NetworkResult.Exception(t)) }
        }
    } else {
        query().collect { send(NetworkResult.Success(it)) }
    }
}