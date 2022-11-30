package com.example.igeniusandroidtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.utils.onLoading
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) :
    ViewModel() {

    private val _repositories = authUserReposRepository.repositories
    val repositories get() = _repositories.asLiveData()
    private val _onLoadingEvent = Channel<Unit>()
    val onLoadingEvent = _onLoadingEvent.receiveAsFlow()

    fun fetchRepositories() {
        viewModelScope.launch {
            authUserReposRepository.getRepositories().collect { result ->
                result
                    .onSuccess { repos -> _repositories.emit(repos) }
                    .onLoading { _onLoadingEvent.send(Unit) }
            }
        }
    }
}