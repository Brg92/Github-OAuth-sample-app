package com.example.igeniusandroidtest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.model.AuthUserReposItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authUserReposRepository: AuthUserReposRepository) :
    ViewModel() {

    private val _repositories by lazy {
        MutableLiveData<List<AuthUserReposItem>>(
            listOf(
                AuthUserReposItem("title1", "desc1", "language1", 1),
                AuthUserReposItem("title2", "desc2", "language2", 2),
                AuthUserReposItem("title3", "desc3", "language3", 3),
                AuthUserReposItem("title4", "desc4", "language4", 4),
            )
        )
    }
    val repositories:LiveData<List<AuthUserReposItem>> get() =  _repositories

}