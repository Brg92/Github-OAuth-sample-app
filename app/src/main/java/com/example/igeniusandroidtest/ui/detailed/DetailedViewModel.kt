package com.example.igeniusandroidtest.ui.detailed

import androidx.lifecycle.*
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.data.repository.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    private val authUserReposRepository: AuthUserReposRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isStarred = MutableLiveData(false)
    val isStarred: LiveData<Boolean> = _isStarred
    val repository = savedStateHandle.getLiveData<Int>("query_get_repository_by_id").switchMap { id ->
        authUserReposRepository.repositories.asLiveData()
            .map { repositories -> repositories?.find { repository -> repository.id == id } }
    }

    fun setQuery(id: Int) {
        savedStateHandle["query_get_repository_by_id"] = id
    }

    suspend fun checkStarredRepository(userName: String, repositoryName: String) {
        when (authUserReposRepository.checkStarredRepository(userName, repositoryName).code()) {
            204 -> _isStarred.postValue(true)
            304 -> _isStarred.postValue(false)
            401 -> Timber.i("Require authentication")
            403 -> Timber.i("Require permission")
            404 -> Timber.e("Not Found if this repository is not starred by you")
            else -> Unit
        }
    }

    fun starRepository(userName: String, repositoryName: String) {
        viewModelScope.launch {
            isStarred.value?.let { isStarred ->
                if (isStarred)
                    unstarApi(userName, repositoryName)?.let { isSuccess ->
                        if (isSuccess)
                            _isStarred.postValue(false)
                    }
                else
                    starApi(userName, repositoryName)?.let { isSuccess ->
                        if (isSuccess)
                            _isStarred.postValue(true)
                    }
            }
        }
    }


    private suspend fun starApi(userName: String, repositoryName: String): Boolean {
        var isSuccess = false
        when (authUserReposRepository.starRepository(userName, repositoryName).code()) {
            204 -> isSuccess = true
            304 -> isSuccess = false
            401 -> Timber.i("Require authentication")
            403 -> Timber.i("Require permission")
            404 -> Timber.e("Resource not found")
            else -> Unit
        }
        return isSuccess
    }

    private suspend fun unstarApi(userName: String, repositoryName: String): Boolean {
        var isSuccess = false
        when (authUserReposRepository.unstarRepository(userName, repositoryName).code()) {
            204 -> isSuccess = true
            304 -> isSuccess = false
            401 -> Timber.i("Require authentication")
            403 -> Timber.i("Require permission")
            404 -> Timber.e("Resource not found")
            else -> Unit
        }
        return isSuccess
    }
}
