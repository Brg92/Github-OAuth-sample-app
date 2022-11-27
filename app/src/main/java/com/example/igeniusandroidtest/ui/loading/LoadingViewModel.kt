package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onException
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val authUserReposRepository: AuthUserReposRepository
) :
    ViewModel() {

    private val _onReposSuccessEvent = MutableStateFlow<Unit?>(null)
    val onReposSuccessEvent = _onReposSuccessEvent.asSharedFlow()

    fun getRepos() {
        viewModelScope.launch {
            authUserReposRepository.getRepos()
                .onSuccess { user -> _onReposSuccessEvent.emit(Unit) }
                .onError { code, message -> Timber.d("error $code, message $message") }
                .onException { Timber.d("exception ${it.message}") }
        }
    }
}