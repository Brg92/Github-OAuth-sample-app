package com.example.igeniusandroidtest.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.utils.onLoading
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val authUserReposRepository: AuthUserReposRepository
) :
    ViewModel() {

    fun getRepositories() {
        viewModelScope.launch {
            authUserReposRepository.getRepositories().collect { result ->
                result
                    .onSuccess { Timber.d("repositories $it") }
            }
        }
    }
}