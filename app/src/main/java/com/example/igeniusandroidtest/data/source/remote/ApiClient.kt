package com.example.igeniusandroidtest.data.source.remote

import com.example.igeniusandroidtest.BuildConfig
import com.example.igeniusandroidtest.utils.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class ApiClient @Inject constructor() {

    private val okHttpClient by lazy { OkHttpClient.Builder().build() }

    val moshi: Moshi by lazy { Moshi.Builder().build() }

    val apiInterface: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }
}