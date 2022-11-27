package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.BuildConfig
import com.example.igeniusandroidtest.model.AccessToken
import com.example.igeniusandroidtest.utils.NetworkResult
import retrofit2.http.*

interface AuthInterface {

    /*
    * It returns the access_token
    * */
    @Headers("Accept: application/json")
    @POST(BuildConfig.TOKEN_ENDPOINT)
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("code") code: String
    ): NetworkResult<AccessToken>
}