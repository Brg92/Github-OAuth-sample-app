package com.example.igeniusandroidtest.data.source.local

import androidx.room.TypeConverter

object Converters {

    @TypeConverter
    fun fromLanguage(any: Any?): String? = any?.toString()

}