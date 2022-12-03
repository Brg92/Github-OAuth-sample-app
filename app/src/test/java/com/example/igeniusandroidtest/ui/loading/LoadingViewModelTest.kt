package com.example.igeniusandroidtest.ui.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.igeniusandroidtest.MainDispatcherTestRule
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepositoryFake
import com.example.igeniusandroidtest.data.source.local.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoadingViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

    @Test
    fun `when getRepositories failed then return error message`() = runTest {
        authUserReposRepositoryFake.setReturnNetworkResult(AuthUserReposRepositoryFake.NetworkResultFake.ERROR)
        val expected = "error code: -1, message: Generic Error Api"

        viewModel.getRepositories()

        advanceTimeBy(1000)
        viewModel.onErrorEvent.test {
            Assert.assertEquals(expected, awaitEvent())
        }
    }
}