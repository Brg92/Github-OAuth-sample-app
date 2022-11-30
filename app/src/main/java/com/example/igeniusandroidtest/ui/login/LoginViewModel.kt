package com.example.igeniusandroidtest.ui.login

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserRepository
import com.example.igeniusandroidtest.utils.Constants
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
class LoginViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    private val encryptedSharedPreferences: SharedPreferences
) :
    ViewModel() {

    private val _onSuccessEvent = MutableStateFlow<Unit?>(null)
    val onSuccessEvent = _onSuccessEvent.asSharedFlow()
    private val _onFailureEvent = MutableStateFlow<String?>(null)
    val onFailureEvent = _onFailureEvent.asSharedFlow()
    val hasAccessToken
        get() = encryptedSharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, null)?.isNotEmpty() != null

    fun getAccessToken(
        client_id: String,
        client_secret: String,
        code: String
    ) {
        viewModelScope.launch {
            authUserRepository.getAccessToken(client_id, client_secret, code)
                .onSuccess {
                    encryptedSharedPreferences.edit {
                        putString(Constants.ACCESS_TOKEN_KEY, "${it.type} ${it.accessToken}")
                    }
                    _onSuccessEvent.emit(Unit)
                }
                .onError { code, message -> _onFailureEvent.emit(message) }
                .onException { Timber.d("exception ${it.message}") }
        }
    }
}