package com.example.igeniusandroidtest.ui.detailed

import androidx.lifecycle.*
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    private val authUserReposRepository: AuthUserReposRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isStarred = MutableLiveData<Boolean>()
    val isStarred: LiveData<Boolean> = _isStarred
    val repository = savedStateHandle.getLiveData<Int>("query").switchMap { id ->
        authUserReposRepository.repositories.asLiveData()
            .map { repositories -> repositories.find { repository -> repository.id == id } }
    }

    fun setQuery(id: Int) {
        savedStateHandle["query"] = id
    }

    fun checkStarredRepository(userName: String, repositoryName: String) {
        viewModelScope.launch {
            val code = authUserReposRepository.checkStarredRepository(userName, repositoryName).code()
            NetworkResultSuccessHelper(code) { isSuccess -> _isStarred.value = isSuccess }()
        }
    }

    fun performButtonStar(userName: String, repositoryName: String) {
        _isStarred.value?.let { condition ->
            if (condition) unstarApi(userName, repositoryName) else starApi(userName, repositoryName)
        }
    }

    private fun starApi(userName: String, repositoryName: String) {
        viewModelScope.launch {
            val code = authUserReposRepository.starRepository(userName, repositoryName).code()
            NetworkResultSuccessHelper(code) { isSuccess -> _isStarred.value = isSuccess }()
        }
    }

    private fun unstarApi(userName: String, repositoryName: String) {
        viewModelScope.launch {
            val code = authUserReposRepository.unstarRepository(userName, repositoryName).code()
            NetworkResultSuccessHelper(code) { isSuccess -> _isStarred.value = !isSuccess }()
        }
    }

    inner class NetworkResultSuccessHelper(
        private val code: Int,
        private val onResultSuccess: (Boolean) -> Unit
    ) {

        operator fun invoke() {
            when (code) {
                204 -> onResultSuccess(true)
                304 -> onResultSuccess(false)
                401 -> Timber.i("Require authentication")
                403 -> Timber.i("Require permission")
                404 -> Timber.e("Resource not found")
                else -> Unit
            }
        }
    }
}


