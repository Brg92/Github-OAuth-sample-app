package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.UserRepository
import com.example.igeniusandroidtest.utils.NetworkResult
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onException
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().onSuccess {
                Timber.d("success, user $it")
            }.onError { code, message ->
                Timber.d("error $code, message $message")
            }.onException {
                Timber.d("exception ${it.message}")
            }
        }
    }
}