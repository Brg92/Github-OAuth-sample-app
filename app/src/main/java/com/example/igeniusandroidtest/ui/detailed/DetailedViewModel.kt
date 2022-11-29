package com.example.igeniusandroidtest.ui.detailed

import androidx.lifecycle.ViewModel
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository): ViewModel() {
}