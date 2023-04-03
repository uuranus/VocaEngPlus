package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.UserWordList
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDataDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import retrofit2.Response
import retrofit2.http.*

interface DatabaseService {

    @GET("/UserData/{uid}.json")
    suspend fun getUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<UserData>

    @PUT("/UserData/{uid}.json")
    suspend fun putUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: UserDataDTO
    ): Response<UserDataDTO>

    @DELETE("/UserData/{uid}.json")
    suspend fun deleteUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    @GET("/WordList/{uid}/.json")
    suspend fun getUserWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO
    ): Response<List<UserWordList>>

    @POST("/WordList/{uid}.json")
    suspend fun postWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO
    ): Response<PostDTO>

    @DELETE("/WordList/{uid}.json")
    suspend fun deleteWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO
    ): Response<String>

}
