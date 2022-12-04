package com.example.igeniusandroidtest.data.source.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    /*
    * thanks to replace conflict strategy, insert and update operations at the same time (use same id).
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<Repository>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repository: Repository)

    @Delete
    suspend fun deleteRepository(repository: Repository)

    @Query("DELETE FROM repository")
    suspend fun deleteAllRepositories()

    @Query("SELECT*FROM repository")
    fun getRepositories(): Flow<List<Repository>>

    @Query("SELECT*FROM repository WHERE id = :id")
    suspend fun getRepositoryById(id: Int): Repository
}