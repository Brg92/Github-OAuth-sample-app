package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response

class AuthUserReposRepositoryFake : AuthUserReposRepository {

    enum class NetworkResultFake {
        SUCCESS, ERROR, EXCEPTION, LOADING
    }

    enum class ResultFake(val code: Int) {
        SUCCESS_POSITIVE(204), SUCCESS_NEGATIVE(304), ERR_FORB(403), ERR_RES_NOT_FOUND(404)
    }

    val repositoryItemsSourceRemote = mutableListOf<Repository>()
    private var networkResult: NetworkResultFake = NetworkResultFake.SUCCESS
    private var result = ResultFake.SUCCESS_POSITIVE
    private val repositoryItemsSourceDatabase = mutableListOf<Repository>()
    private val asyncRepositoryItemsDb: Flow<List<Repository>> = MutableStateFlow(repositoryItemsSourceDatabase)

    fun setReturnNetworkResult(value: NetworkResultFake) {
        networkResult = value
    }

    fun setReturnResponseCodeResult(value: ResultFake) {
        result = value
    }

    private suspend fun networkApiFakeReturnResult(): NetworkResult<List<Repository>> {
        delay(1000)
        return when (networkResult) {
            NetworkResultFake.SUCCESS -> NetworkResult.Success(repositoryItemsSourceRemote)
            NetworkResultFake.ERROR -> NetworkResult.Error(-1, "Generic Error Api")
            NetworkResultFake.EXCEPTION -> NetworkResult.Exception(Throwable("Generic Exception"))
            NetworkResultFake.LOADING -> NetworkResult.Loading()
        }
    }

    private suspend fun networkApiFakeReturnResponse(
        ownerName: String,
        nameRepository: String,
        swap: Boolean? = null
    ): Response<Unit> {
        val hasUser = repositories.value.find { it.name == nameRepository && it.owner?.login == ownerName } != null
        delay(1000)
        if (swap == true) return Response.success(ResultFake.SUCCESS_POSITIVE.code, Unit)
        if (!hasUser) return Response.success(401, Unit)
        return when (result) {
            ResultFake.SUCCESS_POSITIVE -> Response.success(result.code, Unit)
            ResultFake.SUCCESS_NEGATIVE -> Response.success(result.code, Unit)
            ResultFake.ERR_FORB -> Response.success(result.code, Unit)
            ResultFake.ERR_RES_NOT_FOUND -> Response.success(result.code, Unit)
        }
    }

    override val repositories: MutableStateFlow<List<Repository>> = MutableStateFlow(emptyList())

    override suspend fun insertRepository(repository: Repository) {
        repositoryItemsSourceDatabase.add(repository)
    }

    override suspend fun deleteAllRepositories() {
        repositoryItemsSourceDatabase.clear()
    }

    override suspend fun deleteRepository(repository: Repository) {
        repositoryItemsSourceDatabase.remove(repository)
    }

    override fun getRepositories(): Flow<NetworkResult<List<Repository>>> {
        return networkBoundResource(
            query = { asyncRepositoryItemsDb },
            fetch = { networkApiFakeReturnResult() },
            saveFetchResult = { result ->
                result
                    .onSuccess { repos ->
                        repositories.emit(repos)
                        repositoryItemsSourceDatabase.clear()
                        repositoryItemsSourceDatabase.addAll(repos)
                    }
                    .onError { _, _ -> }
                    .onException { _ -> }
            }
        )
    }

    override suspend fun getRepositoryById(id: Int): Repository {
        return repositoryItemsSourceDatabase.first { it.id == id }
    }

    override suspend fun checkStarredRepository(
        ownerName: String,
        nameRepository: String
    ): Response<Unit> {
        return networkApiFakeReturnResponse(ownerName, nameRepository)
    }

    override suspend fun starRepository(ownerName: String, nameRepository: String): Response<Unit> {
        return networkApiFakeReturnResponse(ownerName, nameRepository, true)
    }

    override suspend fun unstarRepository(ownerName: String, nameRepository: String): Response<Unit> {
        return networkApiFakeReturnResponse(ownerName, nameRepository)
    }
}