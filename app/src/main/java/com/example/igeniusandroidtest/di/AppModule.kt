package com.example.igeniusandroidtest.di

import com.example.igeniusandroidtest.data.source.remote.ApiClient
import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIoCoroutineScope() = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    fun provideApiInterface(coroutineScope: CoroutineScope): ApiInterface {
        return ApiClient(coroutineScope).apiInterface
    }

    @Provides
    @Singleton
    fun provideMoshi(coroutineScope: CoroutineScope): Moshi {
        return ApiClient(coroutineScope).moshi
    }

}