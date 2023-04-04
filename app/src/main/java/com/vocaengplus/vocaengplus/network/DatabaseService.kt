package com.vocaengplus.vocaengplus.network

import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.UserWordList
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDataDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import com.vocaengplus.vocaengplus.ui.util.DB_ROOT
import retrofit2.Response
import retrofit2.http.*

interface DatabaseService {

    /**
     * UserData
     */
    @GET("/${DB_ROOT}/UserData/{uid}.json")
    suspend fun getUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<UserData>

    @PUT("/${DB_ROOT}/UserData/{uid}.json")
    suspend fun putUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: UserDataDTO
    ): Response<UserDataDTO>

    @DELETE("/${DB_ROOT}/UserData/{uid}.json")
    suspend fun deleteUserData(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * UserWordList
     */
    @PUT("/${DB_ROOT}/UserWordList/{uid}/{wordListId}.json")
    suspend fun putUserWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
        @Body data: Boolean
    ): Response<Boolean>

    @DELETE("/${DB_ROOT}/UserWordList/{uid}/{wordListId}.json")
    suspend fun deleteUserWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * WordList
     */
    @GET("/${DB_ROOT}/WordList/{uid}/.json")
    suspend fun getWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<List<WordListDTO>>

    @POST("/${DB_ROOT}/WordList/{uid}.json")
    suspend fun postWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO
    ): Response<PostDTO>

    @DELETE("/${DB_ROOT}/WordList/{uid}.json")
    suspend fun deleteWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO
    ): Response<String>

    /**
     * UserLog
     */


    /**
     * Community
     */
}
