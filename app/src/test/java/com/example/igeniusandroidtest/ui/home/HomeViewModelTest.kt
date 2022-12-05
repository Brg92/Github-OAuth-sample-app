package com.example.igeniusandroidtest.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.igeniusandroidtest.MainDispatcherTestRule
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepositoryFake
import com.example.igeniusandroidtest.data.source.local.Repository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherTestRule = MainDispatcherTestRule()

    private lateinit var authUserReposRepositoryFake: AuthUserReposRepositoryFake
    private lateinit var viewModel: HomeViewModel
    private val repositoryItems: MutableList<Repository> =
        mutableListOf(Repository().apply { id = 1 }, Repository().apply { id = 2 }, Repository().apply { id = 3 })

    @Before
    fun setup() {
        authUserReposRepositoryFake = AuthUserReposRepositoryFake()
        viewModel = HomeViewModel(authUserReposRepositoryFake)
    }

    /*
    * The purpose of this test is to simulate the repositories loaded to the api call the previous screen.
    * */
    @Test
    fun `given a preload list of repositories then produceHomeUiState emit repositories with success`() =
        runTest {
            repositoryItems.onEach { repository -> authUserReposRepositoryFake.repositoryItemsSourceRemote.add(repository) }
            authUserReposRepositoryFake.repositories.emit(repositoryItems)

            advanceTimeBy(1)
            Assert.assertEquals(repositoryItems, viewModel.produceHomeUiState.value.cards)
        }

    @Test
    fun `given a preload list of repositories different from the expected then produceHomeUiState emit the expected with success`() =
        runTest {
            repositoryItems.onEach { repository -> authUserReposRepositoryFake.repositoryItemsSourceRemote.add(repository) }
            authUserReposRepositoryFake.repositories.emit(repositoryItems)
            val unexpected =
                mutableListOf(
                    Repository().apply { id = 1 },
                    Repository().apply { id = 2 },
                    Repository().apply { id = 3 },
                    Repository().apply { id = 4 })

            advanceTimeBy(1)
            Assert.assertNotEquals(unexpected, viewModel.produceHomeUiState.value.cards)
        }

    /*
    * The purpose of this test is to simulate the accurate behavior offline.
    * */
    @Test
    fun `given a list of empty repositories when fetchRepositories then produceHomeUiState emit repositories with success`() =
        runTest {
            // load items to the db.
            repositoryItems.onEach { repository -> authUserReposRepositoryFake.insertRepository(repository) }
            // offline mode the api return exception
            authUserReposRepositoryFake.setReturnNetworkResult(AuthUserReposRepositoryFake.NetworkResultFake.EXCEPTION)
            // load repositories from cache
            authUserReposRepositoryFake.getRepositories()

            // Trick for testing private method, with reflection.
            /*val fetch: Method =
                viewModel.javaClass.getDeclaredMethod("fetchRepositories").apply { isAccessible = true }
            fetch.invoke(viewModel)*/

            advanceTimeBy(3001)
            Assert.assertEquals(repositoryItems, viewModel.produceHomeUiState.value.cards)
        }

    @Test
    fun `give a list of empty repositories when fetchRepositories then produceHomeUiState update isEmptyState to true`() = runTest{
        authUserReposRepositoryFake.repositoryItemsSourceRemote.clear()
        authUserReposRepositoryFake.setReturnNetworkResult(AuthUserReposRepositoryFake.NetworkResultFake.EXCEPTION)

        authUserReposRepositoryFake.getRepositories()

        advanceTimeBy(3001)
        Assert.assertEquals(true, viewModel.produceHomeUiState.value.isEmptyState)
    }

    @Test
    fun `give a list of repositories when fetchRepositories then produceHomeUiState update isEmptyState to false`() = runTest{
        repositoryItems.onEach { repository -> authUserReposRepositoryFake.repositoryItemsSourceRemote.add(repository) }
        authUserReposRepositoryFake.repositories.emit(repositoryItems)

        advanceTimeBy(1)
        Assert.assertEquals(false, viewModel.produceHomeUiState.value.isEmptyState)
    }
}