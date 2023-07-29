package com.vocaengplus.vocaengplus.di

import com.vocaengplus.vocaengplus.model.repository.UserRepository
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.service.DatabaseService
import com.vocaengplus.vocaengplus.network.service.SearchAPIService
import com.vocaengplus.vocaengplus.network.service.StorageService
import com.vocaengplus.vocaengplus.util.CLIENT_ID
import com.vocaengplus.vocaengplus.util.CLIENT_SECRET
import com.vocaengplus.vocaengplus.util.DATABASE_BASE_URL
import com.vocaengplus.vocaengplus.util.STORAGE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val interceptor = Interceptor {
        val newRequest = it
            .request()
            .newBuilder()
            .addHeader("X-Naver-Client-Id", CLIENT_ID)
            .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
            .build()
        it.proceed(newRequest)
    }

    @Qualifier
    annotation class StorageRetrofit

    @Qualifier
    annotation class DatabaseRetrofit

    @Qualifier
    annotation class SearchRetrofit

    @Qualifier
    annotation class VocaEngPlusOkHttpClient

    @Qualifier
    annotation class SearchOkHttpClient


    @VocaEngPlusOkHttpClient
    @Provides
    @Singleton
    fun provideVocaEngPlusOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @SearchOkHttpClient
    @Provides
    @Singleton
    fun provideSearchOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @DatabaseRetrofit
    @Provides
    @Singleton
    fun provideDatabaseRetrofit(@VocaEngPlusOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DATABASE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @StorageRetrofit
    @Provides
    @Singleton
    fun provideStorageRetrofit(@VocaEngPlusOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(STORAGE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @SearchRetrofit
//    @Provides
//    @Singleton
//    fun provideSearchRetrofit(@SearchOkHttpClient okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(SEARCH_API_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

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
    fun providesSearchService() = SearchAPIService()

    @Provides
    @Singleton
    fun providesAuthService() = AuthService

    @Provides
    @Singleton
    fun provideUserRepository() = UserRepository()
}