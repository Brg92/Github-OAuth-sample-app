package com.example.igeniusandroidtest.ui.home

import androidx.lifecycle.ViewModel
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) :
    ViewModel() {

}