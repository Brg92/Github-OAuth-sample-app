package com.example.igeniusandroidtest.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.example.igeniusandroidtest.MainDispatcherTestRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class RepositoryDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherTestRule = MainDispatcherTestRule()

    private lateinit var database: RepositoryDatabase
    private lateinit var dao: RepositoryDao
    private val repositoryItem = Repository().apply {
        id = 100
        owner = Owner(null, "Androidtest", null, null)
    }

    @Before
    fun setup() {
        database = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RepositoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRepositoryItem() = runTest {
        database.dao.insertRepository(repositoryItem)

        dao.getRepositories().test {
            assertThat(awaitItem()).contains(repositoryItem)
        }
    }
}