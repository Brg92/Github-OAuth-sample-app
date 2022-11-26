package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor() :
    ViewModel() {

    /* fun getUser() {
         viewModelScope.launch {
             userRepository.getUser().onSuccess {
                 Timber.d("success, user $it")
             }.onError { code, message ->
                 Timber.d("error $code, message $message")
             }.onException {
                 Timber.d("exception ${it.message}")
             }
         }
     }*/
}