package com.vocaengplus.vocaengplus.di

import com.vocaengplus.vocaengplus.network.StorageService
import com.vocaengplus.vocaengplus.network.VocaEngPlusService
import com.vocaengplus.vocaengplus.ui.util.VOCAENGPLUS_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class VocaEngPlusRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class StorageRetrofit

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(VOCAENGPLUS_BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesVocaEngPlusService(@VocaEngPlusRetrofit retrofit: Retrofit): VocaEngPlusService {
        return retrofit.create(VocaEngPlusService::class.java)
    }

    @Provides
    @Singleton
    fun providesStorageService(@StorageRetrofit retrofit: Retrofit): StorageService {
        return retrofit.create(StorageService::class.java)
    }

}