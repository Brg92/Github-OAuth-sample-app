package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.model.AuthUserRepos
import com.example.igeniusandroidtest.utils.NetworkResult

interface AuthUserReposRepository {

    suspend fun getRepos(): NetworkResult<AuthUserRepos>
}