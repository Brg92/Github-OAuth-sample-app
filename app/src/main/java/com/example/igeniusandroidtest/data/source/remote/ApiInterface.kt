package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.model.AuthUser
import com.example.igeniusandroidtest.model.AuthUserRepos
import com.example.igeniusandroidtest.utils.NetworkResult
import retrofit2.http.*

interface ApiInterface {

    @GET("/user")
    suspend fun getUser(): NetworkResult<AuthUser>

    @GET("/user/repos")
    suspend fun getRepos(): NetworkResult<AuthUserRepos>

    /*
     @GET("/search/repositories") // search/repositories
     suspend fun getRepos(@Query("q") user: String): NetworkResult<AuthUserRepos>
     */
}