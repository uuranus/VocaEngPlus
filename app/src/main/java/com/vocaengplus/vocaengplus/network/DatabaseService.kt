package com.vocaengplus.vocaengplus.network

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vocaengplus.vocaengplus.model.data.new.UserData
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.ui.util.DB_ROOT_KEY
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

    @GET("/UserWordList/{uid}")
    fun getUserWordList(
        @Query("auth") token: String,
        @Path("uid") uid: String
    ): Response<UserWordList>

}