package com.example.igeniusandroidtest.data.source.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromLanguage(any: Any?): String? = any?.toString()

}