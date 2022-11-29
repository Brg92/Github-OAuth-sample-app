package com.example.igeniusandroidtest.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/*
* every time the db change, increase version.
* */
@Database(entities = [Repository::class], version = 1)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract val dao: RepositoryDao
}