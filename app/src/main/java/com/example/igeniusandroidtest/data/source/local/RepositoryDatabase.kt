package com.example.igeniusandroidtest.data.source.local

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

/*
* every time the db change, increase version.
* */
@Database(
    entities = [Repository::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(
        from = 2,
        to = 3,
        spec = RepositoryDatabase.Migration2to3::class
    )],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract val dao: RepositoryDao

    @DeleteColumn(tableName = "Repository", columnName = "private")
    class Migration2to3 : AutoMigrationSpec
}