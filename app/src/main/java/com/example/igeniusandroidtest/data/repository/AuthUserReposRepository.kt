package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface AuthUserReposRepository {

    val repositories: StateFlow<List<Repository>>

    suspend fun insertRepository(repository: Repository)
    suspend fun deleteAllRepositories()
    suspend fun deleteRepository(repository: Repository)
    fun getRepositories(): Flow<NetworkResult<List<Repository>>>
    suspend fun getRepositoryById(id: Int): Repository
    suspend fun checkStarredRepository(ownerName: String, nameRepository: String): Response<Unit>
    suspend fun starRepository(ownerName: String, nameRepository: String): Response<Unit>
    suspend fun unstarRepository(ownerName: String, nameRepository: String): Response<Unit>
}