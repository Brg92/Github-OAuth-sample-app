package com.example.igeniusandroidtest.di

import com.example.igeniusandroidtest.data.source.remote.ApiClient
import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface {
        return ApiClient().apiInterface
    }
}