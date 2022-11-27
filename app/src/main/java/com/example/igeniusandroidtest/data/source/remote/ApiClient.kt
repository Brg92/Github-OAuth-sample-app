package com.example.igeniusandroidtest.data.source.remote

import android.content.SharedPreferences
import com.example.igeniusandroidtest.BuildConfig
import com.example.igeniusandroidtest.utils.Constants
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val encryptedSharedPreferences: SharedPreferences
) {

    private val accessToken by lazy {
        encryptedSharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, null)
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    private val okHttpClientAuth by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val okHttpClientApi by lazy {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().apply {
                        addHeader("Accept", "application/vnd.github+json")
                        accessToken?.let { token ->
                            addHeader("Authorization", token)
                        }
                    }.build()
                )
            })
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val moshi: Moshi by lazy { Moshi.Builder().build() }

    val authInterface: AuthInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create(coroutineScope))
            .client(okHttpClientAuth)
            .build()
            .create(AuthInterface::class.java)
    }

    val apiInterface: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create(coroutineScope))
            .client(okHttpClientApi)
            .build()
            .create(ApiInterface::class.java)
    }
}