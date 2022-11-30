package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onException
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) : ViewModel() {

    private val _apiSuccessEvent = MutableSharedFlow<Unit>()
    val apiSuccessEvent get() = _apiSuccessEvent.asSharedFlow()

    fun getRepositories() {
        viewModelScope.launch {
            delay(2000)
            authUserReposRepository.getRepositories().collect { result ->
                result.onSuccess {
                    Timber.d("list $it")
                    _apiSuccessEvent.emit(Unit)
                }.onError { _, message ->
                    Timber.d("error message: $message")
                }.onException {
                    Timber.d("exception ${it.message}")
                }
            }
        }
    }


}