package com.example.igeniusandroidtest.data.source.remote

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class ApiClient @Inject constructor() {

    val apiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}