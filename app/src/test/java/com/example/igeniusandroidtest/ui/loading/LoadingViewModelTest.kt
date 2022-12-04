package com.example.igeniusandroidtest.ui.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.igeniusandroidtest.MainDispatcherTestRule
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepositoryFake
import com.example.igeniusandroidtest.data.source.local.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoadingViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    /*
     * Rule for setting the main Dispatcher in order to test viewModel coroutines launched in viewModelScope over
     * the Ui(Main) thread which is by default running on Android device and no available here.
     * */
    @get:Rule
    var mainDispatcherTestRule = MainDispatcherTestRule()

    private lateinit var authUserReposRepositoryFake: AuthUserReposRepositoryFake
    private lateinit var viewModel: LoadingViewModel
    private val repositoryItems: MutableList<Repository> =
        mutableListOf(Repository().apply { id = 1 }, Repository().apply { id = 2 }, Repository().apply { id = 3 })

    @Before
    fun setup() {
        authUserReposRepositoryFake = AuthUserReposRepositoryFake().also {
            repositoryItems.forEach { repo -> it.repositoryItemsSourceRemote.add(repo) }
        }
        viewModel = LoadingViewModel(authUserReposRepositoryFake)
    }

    @Test
    fun `given a list of repositories when getRepositories succeed then send repositories as success event`() =
        runTest {

            viewModel.getRepositories()

            viewModel.onSuccessEvent.test {
                Assert.assertEquals(repositoryItems, awaitItem())
            }
        }
}