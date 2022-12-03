package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) : ViewModel() {

    private val _onSuccessEvent = Channel<Unit>()
    val onSuccessEvent = _onSuccessEvent.receiveAsFlow()
    private val _onErrorEvent = Channel<String>()
    val onErrorEvent = _onErrorEvent.receiveAsFlow()

    /*
    * The idea behind this api triggered here, it is to pre-load repositories to show faster landing on the home.
    * */
    fun getRepositories() {
        viewModelScope.launch {
            delay(1000)
            authUserReposRepository.getRepositories().collect { networkResult ->
                networkResult.onSuccess { _onSuccessEvent.send(Unit) }
                networkResult.onError { code, message -> _onErrorEvent.send("error code: $code, message: $message") }
            }
        }
    }
}