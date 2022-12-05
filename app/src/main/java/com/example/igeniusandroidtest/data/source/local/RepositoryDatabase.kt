package com.example.igeniusandroidtest.data.source.local

import androidx.room.*

/*
* every time the db change, increase version.
* */
@Database(
    entities = [Repository::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = DatabaseMigrations.Migration2to3::class),
        AutoMigration(from = 3, to = 4, spec = DatabaseMigrations.Migration3to4::class),
        AutoMigration(from = 4, to = 5)
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract val dao: RepositoryDao

}