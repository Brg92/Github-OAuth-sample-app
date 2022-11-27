package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.example.igeniusandroidtest.data.source.remote.AuthInterface
import com.example.igeniusandroidtest.model.AccessToken
import com.example.igeniusandroidtest.model.AuthUser
import com.example.igeniusandroidtest.utils.NetworkResult
import javax.inject.Inject

class AuthUserRepositoryImpl @Inject constructor(
    private val authInterface: AuthInterface,
    private val apiInterface: ApiInterface
) :
    AuthUserRepository {

    override suspend fun getAccessToken(
        client_id: String,
        client_secret: String,
        code: String
    ): NetworkResult<AccessToken> {
        return authInterface.getAccessToken(client_id, client_secret, code)
    }

    override suspend fun getUser(): NetworkResult<AuthUser> {
        return apiInterface.getUser()
    }
}