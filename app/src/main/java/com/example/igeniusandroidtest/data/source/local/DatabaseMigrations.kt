package com.example.igeniusandroidtest.data.source.local

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

object DatabaseMigrations {

    @DeleteColumn(tableName = "Repository", columnName = "private")
    class Migration2to3 : AutoMigrationSpec

    @DeleteColumn(tableName = "Repository", columnName = "private")
    class Migration3to4 : AutoMigrationSpec
}