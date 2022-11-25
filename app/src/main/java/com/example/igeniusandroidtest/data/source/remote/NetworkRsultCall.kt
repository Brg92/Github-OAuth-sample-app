package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.utils.NetworkResult
import com.example.igeniusandroidtest.utils.handleApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class NetworkResultCall<T : Any> @Inject constructor(
    private val proxy: Call<T>,
    private val coroutineScope: CoroutineScope
) : Call<NetworkResult<T>> {

    override fun enqueue(callback: Callback<NetworkResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                coroutineScope.launch {
                    try {
                        val networkResult = handleApi { response }
                        callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
                    } catch (e: Exception) {
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(NetworkResult.Exception(e))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@NetworkResultCall,
                    Response.success(NetworkResult.Exception(t))
                )
            }
        })
    }

    override fun execute(): Response<NetworkResult<T>> = throw NotImplementedError()
    override fun clone(): Call<NetworkResult<T>> = NetworkResultCall(proxy.clone(), coroutineScope)
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() = proxy.cancel()
}