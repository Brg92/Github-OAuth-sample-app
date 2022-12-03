package com.example.igeniusandroidtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.utils.onError
import com.example.igeniusandroidtest.utils.onLoading
import com.example.igeniusandroidtest.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val cards: List<Repository> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) :
    ViewModel() {

    private val _produceHomeUiState = MutableStateFlow(HomeUiState())
    val produceHomeUiState: StateFlow<HomeUiState> = _produceHomeUiState

    private val _repositories = authUserReposRepository.repositories
        .map { repos -> _produceHomeUiState.value.copy(cards = repos) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, _produceHomeUiState.value)

    init {
        /*
        * The fetch is triggered if there is no connection, so the pre-load repositories are not available and it is required
        * access the cached repositories.
        * */
        viewModelScope.launch {
            if (_repositories.value.cards.isNotEmpty())
                _produceHomeUiState.emit(_repositories.value)
            else
                fetchRepositories()
        }
    }

     private fun fetchRepositories() {
        viewModelScope.launch {
            authUserReposRepository.getRepositories().collect { networkResult ->
                networkResult.onSuccess { repos -> _produceHomeUiState.update { it.copy(cards = repos, isLoading = false) } }
                networkResult.onLoading { _produceHomeUiState.update { it.copy(isLoading = true) } }
                networkResult.onError { code, message ->
                    _produceHomeUiState.update {
                        it.copy(
                            isLoading = false,
                            error = "error code: $code, message: $message"
                        )
                    }
                }
            }
        }
    }
}