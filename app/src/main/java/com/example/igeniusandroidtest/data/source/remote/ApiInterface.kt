package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.model.AuthUser
import com.example.igeniusandroidtest.utils.NetworkResult
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @Headers("Accept: application/vnd.github+json")
    @GET("/user")
    suspend fun getUser(): NetworkResult<AuthUser>

    @Headers("Accept: application/vnd.github+json")
    @GET("/user/repos")
    suspend fun getRepos(): NetworkResult<List<Repository>>

    @Headers("Content-Length: 0")
    @GET("/user/starred/{owner}/{repo}")
    suspend fun checkStarredRepository(
        @Path("owner") nameOwner: String,
        @Path("repo") nameRepository: String
    ): Response<Unit>

    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    suspend fun starRepository(
        @Path("owner") nameOwner: String,
        @Path("repo") nameRepository: String
    ): Response<Unit>

    @Headers("Content-Length: 0")
    @DELETE("/user/starred/{owner}/{repo}")
    suspend fun unstarRepository(
        @Path("owner") nameOwner: String,
        @Path("repo") nameRepository: String
    ): Response<Unit>

}