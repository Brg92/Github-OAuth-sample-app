package com.example.igeniusandroidtest.data.repository

import android.content.Context
import android.widget.Toast
import androidx.room.withTransaction
import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.data.source.local.RepositoryDatabase
import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.example.igeniusandroidtest.utils.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class AuthUserReposRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiInterface: ApiInterface, private val db: RepositoryDatabase
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

    override fun getRepositories(): Flow<NetworkResult<List<Repository>>> {
        return networkBoundResource(
            query = { db.dao.getRepositories() },
            fetch = { apiInterface.getRepos() },
            saveFetchResult = { result ->
                db.withTransaction {
                    result.onSuccess { repos ->
                        db.dao.deleteAllRepositories()
                        db.dao.insertRepositories(repos)
                    }.onError { code, message ->
                        Toast.makeText(
                            context,
                            "Error code: $code, message: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.onException { Timber.e(it.message) }
                }
            }
        )
    }

    override suspend fun getRepositoryById(id: Int): Repository {
        return db.dao.getRepositoryById(id)
    }
}