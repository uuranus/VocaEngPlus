package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.UserWordList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DatabaseService {

    @GET("/UserData/{uid}.json")
    fun getUserData(
        @Query("auth") token: String,
        @Path("uid") uid: String
    ): Response<UserData>

    @GET("/UserWordList/{uid}.json")
    fun getUserWordList(
        @Query("auth") token: String,
        @Path("uid") uid: String
    ): Response<List<UserWordList>>

}