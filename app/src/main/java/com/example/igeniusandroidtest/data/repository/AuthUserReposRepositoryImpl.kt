package com.example.igeniusandroidtest.data.repository

import androidx.room.withTransaction
import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.data.source.local.RepositoryDatabase
import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.example.igeniusandroidtest.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthUserReposRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val db: RepositoryDatabase
) : AuthUserReposRepository {

    override suspend fun insertRepository(repository: Repository) {
        db.dao.insertRepository(repository)
    }

    override suspend fun deleteAllRepositories() {
        return db.dao.deleteAllRepositories()
    }

    override suspend fun deleteRepository(repository: Repository) {
        db.dao.deleteRepository(repository)
    }

    override val repositories = MutableStateFlow<List<Repository>>(emptyList())

    override fun getRepositories(): Flow<NetworkResult<List<Repository>>> {
        return networkBoundResource(
            query = { db.dao.getRepositories() },
            fetch = { apiInterface.getRepos() },
            saveFetchResult = { result ->
                result
                    .onSuccess { repos ->
                        repositories.emit(repos)
                        db.withTransaction {
                            db.dao.deleteAllRepositories()
                            db.dao.insertRepositories(repos)
                        }
                    }
                    .onError { code, message -> Timber.e("Error code: $code, message: $message") }
                    .onException { t -> Timber.e(t.message) }
            }
        )
    }

    override suspend fun getRepositoryById(id: Int): Repository {
        return db.dao.getRepositoryById(id)
    }

    override suspend fun checkStarredRepository(
        ownerName: String,
        nameRepository: String
    ): Result<Int> {
        return apiInterface.checkStarredRepository(ownerName, nameRepository)
    }

    override suspend fun starRepository(nameOwner: String, nameRepository: String): Result<Int> {
        return apiInterface.starRepository(nameOwner, nameRepository)
    }

    override suspend fun unstarRepository(nameOwner: String, nameRepository: String): Result<Int> {
        return apiInterface.unstarRepository(nameOwner, nameRepository)
    }
}