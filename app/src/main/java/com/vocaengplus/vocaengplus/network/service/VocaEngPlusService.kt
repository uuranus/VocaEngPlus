package com.vocaengplus.vocaengplus.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VocaEngPlusService {

    @GET("/UserData/{uid}/")
    suspend fun getUserInfo(
        @Query("auth") auth: String,
        @Path("uid") uid: String,
    )

    @GET("/Words/{id}")
    suspend fun getWordById(
        @Path("id") id: String,
        @Query("auth") auth: String)
}