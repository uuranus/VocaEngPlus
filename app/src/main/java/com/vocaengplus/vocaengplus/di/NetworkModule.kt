package com.vocaengplus.vocaengplus.di

import com.google.gson.GsonBuilder
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.StorageService
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.ui.util.DATABSE_BASE_URL
import com.vocaengplus.vocaengplus.ui.util.STORAGE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    annotation class StorageRetrofit

    @Qualifier
    annotation class DatabaseRetrofit

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @DatabaseRetrofit
    @Provides
    @Singleton
    fun provideDatabaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DATABSE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @StorageRetrofit
    @Provides
    @Singleton
    fun provideStorageRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(STORAGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesDatabaseService(@DatabaseRetrofit retrofit: Retrofit): DatabaseService {
        return retrofit.create(DatabaseService::class.java)
    }

    @Provides
    @Singleton
    fun providesStorageService(@StorageRetrofit retrofit: Retrofit): StorageService {
        return retrofit.create(StorageService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthService() = AuthService

}