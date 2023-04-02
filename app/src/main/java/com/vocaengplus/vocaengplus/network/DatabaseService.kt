package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.UserWordList
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDataDTO
import retrofit2.Response
import retrofit2.http.*

interface DatabaseService {

    @GET("/UserData/{uid}.json")
    fun getUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<UserData>

    @GET("/UserWordList/{uid}.json")
    fun getUserWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<List<UserWordList>>

    @POST("/UserData/{uid}.json")
    fun postUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: UserDataDTO
    ): Response<PostDTO>
}
