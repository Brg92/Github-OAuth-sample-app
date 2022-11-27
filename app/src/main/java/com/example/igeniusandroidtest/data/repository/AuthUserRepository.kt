package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.model.AccessToken
import com.example.igeniusandroidtest.model.AuthUser
import com.example.igeniusandroidtest.utils.NetworkResult

interface AuthUserRepository {

    suspend fun getAccessToken(
        client_id: String,
        client_secret: String,
        code: String
    ): NetworkResult<AccessToken>

    suspend fun getUser(): NetworkResult<AuthUser>
}