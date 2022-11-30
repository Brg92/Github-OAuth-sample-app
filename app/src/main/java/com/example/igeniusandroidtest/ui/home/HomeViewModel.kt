package com.example.igeniusandroidtest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onException
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) :
    ViewModel() {

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories:LiveData<List<Repository>> get() = _repositories

    init {
        getRepositories()
    }

    private fun getRepositories() {
        viewModelScope.launch {
            authUserReposRepository.getRepositories().collect { result ->
                result.onSuccess {
                    Timber.d("list $it")
                    _repositories.postValue(it)
                }.onError { code, message ->
                    Timber.d("error message: $message")
                }.onException {
                    Timber.d("exception ${it.message}")
                }
            }
        }
    }

}