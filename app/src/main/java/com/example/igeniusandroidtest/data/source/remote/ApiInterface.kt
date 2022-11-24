package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.utils.NetworkResult
import retrofit2.http.GET

interface ApiInterface {

    @GET("/users/{user}")
    fun getUser():NetworkResult<Any>
}