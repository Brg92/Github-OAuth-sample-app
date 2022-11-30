package com.example.igeniusandroidtest.ui.detailed

import androidx.lifecycle.*
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    private val authUserReposRepository: AuthUserReposRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val repository = savedStateHandle.getLiveData<Int>("query_get_repository_by_id").switchMap { id ->
        authUserReposRepository.repositories.asLiveData()
            .map { repositories -> repositories?.find { repository -> repository.id == id } }
    }

    fun setQuery(id: Int) {
        savedStateHandle["query_get_repository_by_id"] = id
    }
}