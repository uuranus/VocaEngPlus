package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.model.data.newData.User
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import retrofit2.Response
import retrofit2.http.*

interface DatabaseService {

    /**
     * UserData
     */
    @GET("/UserData/{uid}.json")
    suspend fun getUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<User>

    @PUT("/UserData/{uid}.json")
    suspend fun putUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: UserDTO,
    ): Response<UserDTO>

    @DELETE("/UserData/{uid}.json")
    suspend fun deleteUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * UserWordList
     */
    @GET("/UserWordList/{uid}.json")
    suspend fun getUserWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<Map<String,Any>>

    @PUT("/UserWordList/{uid}/{wordListId}.json")
    suspend fun putUserWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
        @Body data: Boolean,
    ): Response<Boolean>

    @DELETE("/UserWordList/{uid}/{wordListId}.json")
    suspend fun deleteUserWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
    ): Response<String>

    @DELETE("/UserWordList/{uid}.json")
    suspend fun deleteAllUserWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * WordList
     */
    @GET("/WordList/{uid}/{wordListUid}.json")
    suspend fun getWordList(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
    ): Response<WordListDTO>

    @PUT("/WordList/{uid}/{wordListUid}.json")
    suspend fun putWordList(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO,
    ): Response<WordListDTO>

    @POST("/WordList/{uid}.json")
    suspend fun postWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO,
    ): Response<PostDTO>

    @DELETE("/WordList/{uid}.json")
    suspend fun deleteWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO,
    ): Response<String>

    /**
     * UserLog
     */


    /**
     * Community
     */
}
