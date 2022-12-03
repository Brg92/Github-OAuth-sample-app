package com.example.igeniusandroidtest.data.repository

import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class AuthUserReposRepositoryFake : AuthUserReposRepository {

    enum class NetworkResultFake {
        SUCCESS, ERROR, EXCEPTION, LOADING
    }

    enum class Result(val code: Int) {
        SUCCESS_POSITIVE(204), SUCCESS_NEGATIVE(304), ERROR_REQ_AUTH(401), ERR_FORB(403), ERR_RES_NOT_FOUND(404)
    }

    val repositoryItemsFromServer = mutableListOf<Repository>()
    private val repositoryItemsFromDatabase = mutableListOf<Repository>()
    private val asyncRepositoryItemsDb: Flow<List<Repository>> = MutableStateFlow(repositoryItemsFromDatabase)
    private var networkResult: NetworkResultFake = NetworkResultFake.SUCCESS
    private var result = Result.SUCCESS_POSITIVE

    fun setReturnNetworkResult(value: NetworkResultFake) {
        networkResult = value
    }

    fun setReturnResponseCode(value: Result) {
        result = value
    }

    private suspend fun networkApiFakeReturnResult(): NetworkResult<List<Repository>> {
        delay(1000)
        return when (networkResult) {
            NetworkResultFake.SUCCESS -> NetworkResult.Success(repositoryItemsFromServer)
            NetworkResultFake.ERROR -> NetworkResult.Error(-1, "Generic Error Api")
            NetworkResultFake.EXCEPTION -> NetworkResult.Exception(Throwable("Generic Exception"))
            NetworkResultFake.LOADING -> NetworkResult.Loading()
        }
    }

    private suspend fun networkApiFakeReturnResponse(): Response<Unit> {
        delay(1000)
        return when (result) {
            Result.SUCCESS_POSITIVE -> Response.success(result.code,Unit)
            Result.SUCCESS_NEGATIVE ->  Response.success(result.code,Unit)
            Result.ERROR_REQ_AUTH ->  Response.success(result.code,Unit)
            Result.ERR_FORB ->  Response.success(result.code,Unit)
            Result.ERR_RES_NOT_FOUND ->  Response.success(result.code,Unit)
        }
    }

    override val repositories: MutableStateFlow<List<Repository>> = MutableStateFlow(repositoryItemsFromServer)

    override suspend fun insertRepository(repository: Repository) {
        repositoryItemsFromDatabase.add(repository)
    }

    override suspend fun deleteAllRepositories() {
        repositoryItemsFromDatabase.clear()
    }

    override suspend fun deleteRepository(repository: Repository) {
        repositoryItemsFromDatabase.remove(repository)
    }

    override fun getRepositories(): Flow<NetworkResult<List<Repository>>> {
        return networkBoundResource(
            query = { asyncRepositoryItemsDb },
            fetch = { networkApiFakeReturnResult() },
            saveFetchResult = { result ->
                result
                    .onSuccess { repos ->
                        repositories.emit(repos)
                        repositoryItemsFromDatabase.clear()
                        repositoryItemsFromDatabase.addAll(repos)
                    }
                    .onError { code, message -> }
                    .onException { t -> }
            }
        )
    }

    override suspend fun getRepositoryById(id: Int): Repository {
        return repositoryItemsFromDatabase.first { it.id == id }
    }

    override suspend fun checkStarredRepository(
        ownerName: String,
        nameRepository: String
    ): Response<Unit> {
        return networkApiFakeReturnResponse()
    }

    override suspend fun starRepository(ownerName: String, nameRepository: String): Response<Unit> {
        return networkApiFakeReturnResponse()
    }

    override suspend fun unstarRepository(ownerName: String, nameRepository: String): Response<Unit> {
        return networkApiFakeReturnResponse()
    }
}