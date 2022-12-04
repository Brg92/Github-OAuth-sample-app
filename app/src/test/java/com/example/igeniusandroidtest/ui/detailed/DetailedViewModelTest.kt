package com.example.igeniusandroidtest.ui.detailed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.example.igeniusandroidtest.MainDispatcherTestRule
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepositoryFake
import com.example.igeniusandroidtest.data.source.local.Owner
import com.example.igeniusandroidtest.data.source.local.Repository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailedViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherTestRule = MainDispatcherTestRule()

    @MockK
    private lateinit var observerRepository: Observer<Repository?>

    @MockK
    private lateinit var observerStar: Observer<Boolean>

    private lateinit var authUserReposRepositoryFake: AuthUserReposRepositoryFake
    private lateinit var viewModel: DetailedViewModel
    private val repositoryItems: MutableList<Repository> =
        mutableListOf(Repository().apply { id = 1 }, Repository().apply { id = 2 }, Repository().apply {
            id = 100
            owner = Owner(login = "username", avatar_url = null, url = null, type = null)
            name = "repository"
        })
    private val repositoryItem = Repository().apply {
        id = 100
        owner = Owner(login = "username", avatar_url = null, url = null, type = null)
        name = "repository"
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        authUserReposRepositoryFake = AuthUserReposRepositoryFake()
        viewModel = DetailedViewModel(authUserReposRepositoryFake, SavedStateHandle())
    }

    @Test
    fun `given a user with starred repository then check repository is starred`() = runTest {
        authUserReposRepositoryFake.repositories.emit(repositoryItems)

        viewModel.checkStarredRepository(repositoryItem.owner?.login!!, repositoryItem.name!!)
        viewModel.isStarred.observeForever(observerStar)

        advanceTimeBy(1001)
        Assert.assertEquals(true, viewModel.isStarred.value)
        verify { observerStar.onChanged(true) }
    }

    @Test
    fun `given a user with not starred repository then check repository is not starred`() = runTest {
        authUserReposRepositoryFake.repositories.emit(repositoryItems)
        authUserReposRepositoryFake.setReturnResponseCodeResult(AuthUserReposRepositoryFake.ResultFake.SUCCESS_NEGATIVE)

        viewModel.checkStarredRepository(repositoryItem.owner?.login!!, repositoryItem.name!!)
        viewModel.isStarred.observeForever(observerStar)

        advanceTimeBy(1100)
        Assert.assertNotEquals(true, viewModel.isStarred.value)
        verify { observerStar.onChanged(false) }
    }

    @Test
    fun `given a user with starred repository when unstar then star removed`() = runTest {
        authUserReposRepositoryFake.repositories.emit(repositoryItems)
        val slot = slot<Boolean>()
        val slots = mutableListOf<Boolean>()

        every { observerStar.onChanged(capture(slot)) } answers { slots.add(slot.captured) }

        viewModel.checkStarredRepository(repositoryItem.owner?.login!!, repositoryItem.name!!)
        viewModel.isStarred.observeForever(observerStar)

        advanceTimeBy(1100)
        viewModel.performButtonStar(repositoryItem.owner?.login!!, repositoryItem.name!!)

        advanceTimeBy(1100)
        Assert.assertEquals(false, viewModel.isStarred.value)
        verifySequence { slots.forEach { slot -> observerStar.onChanged(slot) } }
    }

    @Test
    fun `given a user with not starred repository when star then star added`() = runTest {
        authUserReposRepositoryFake.repositories.emit(repositoryItems)
        authUserReposRepositoryFake.setReturnResponseCodeResult(AuthUserReposRepositoryFake.ResultFake.SUCCESS_NEGATIVE)
        val slot = slot<Boolean>()
        val slots = mutableListOf<Boolean>()

        every { observerStar.onChanged(capture(slot)) } answers { slots.add(slot.captured) }

        viewModel.checkStarredRepository(repositoryItem.owner?.login!!, repositoryItem.name!!)

        advanceTimeBy(1100)
        viewModel.performButtonStar(repositoryItem.owner?.login!!, repositoryItem.name!!)
        viewModel.isStarred.observeForever(observerStar)

        advanceTimeBy(1100)
        Assert.assertEquals(true, viewModel.isStarred.value)
        verifySequence { slots.forEach { slot -> observerStar.onChanged(slot) } }
    }
}