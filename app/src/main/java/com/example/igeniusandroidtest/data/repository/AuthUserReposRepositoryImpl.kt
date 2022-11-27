package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.example.igeniusandroidtest.model.AuthUserRepos
import com.example.igeniusandroidtest.utils.NetworkResult
import javax.inject.Inject

class AuthUserReposRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) :
    AuthUserReposRepository {

    override suspend fun getRepos(): NetworkResult<AuthUserRepos> {
        return apiInterface.getRepos()
    }
}