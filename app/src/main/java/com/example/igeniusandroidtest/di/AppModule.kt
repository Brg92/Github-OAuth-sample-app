package com.example.igeniusandroidtest.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepository
import com.example.igeniusandroidtest.data.repository.AuthUserReposRepositoryImpl
import com.example.igeniusandroidtest.data.repository.AuthUserRepository
import com.example.igeniusandroidtest.data.repository.AuthUserRepositoryImpl
import com.example.igeniusandroidtest.data.source.remote.ApiClient
import com.example.igeniusandroidtest.data.source.remote.ApiInterface
import com.example.igeniusandroidtest.data.source.remote.AuthInterface
import com.example.igeniusandroidtest.utils.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideApiInterface(
        coroutineScope: CoroutineScope,
        encryptedSharedPreferences: SharedPreferences
    ): ApiInterface {
        return ApiClient(coroutineScope, encryptedSharedPreferences).apiInterface
    }

    @Provides
    @Singleton
    fun provideAuthInterface(coroutineScope: CoroutineScope,encryptedSharedPreferences: SharedPreferences): AuthInterface {
        return ApiClient(coroutineScope,encryptedSharedPreferences).authInterface
    }

    @Provides
    @Singleton
    fun provideMoshi(coroutineScope: CoroutineScope,encryptedSharedPreferences: SharedPreferences): Moshi {
        return ApiClient(coroutineScope,encryptedSharedPreferences).moshi
    }

    @Provides
    @Singleton
    fun provideAuthUserRepository(authInterface: AuthInterface,apiInterface: ApiInterface): AuthUserRepository {
        return AuthUserRepositoryImpl(authInterface,apiInterface)
    }

    @Provides
    @Singleton
    fun provideAuthUserReposRepository(apiInterface: ApiInterface): AuthUserReposRepository {
        return AuthUserReposRepositoryImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun getEncryptedSharedPreferences(@ApplicationContext context: Context) =
        EncryptedSharedPreferences.create(
            Constants.NAME_ENCRYPTED_SHARED_PREFERENCES,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
}