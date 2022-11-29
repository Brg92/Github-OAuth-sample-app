package com.example.igeniusandroidtest.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repository(
    val name: String?,
    val description: String?,
    val language: String?,
    @PrimaryKey val id: Int?
)
