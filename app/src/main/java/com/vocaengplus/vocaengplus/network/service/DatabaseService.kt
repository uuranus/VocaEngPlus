package com.vocaengplus.vocaengplus.network.service

import com.vocaengplus.vocaengplus.model.data.User
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.TestResultLogDTO
import com.vocaengplus.vocaengplus.network.dto.UserDTO
import com.vocaengplus.vocaengplus.network.dto.WordDTO
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
    suspend fun getWordLists(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<Map<String, WordListDTO>>

    @GET("/UserWordList/{uid}/{wordListUid}.json")
    suspend fun getWordList(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
    ): Response<WordListDTO>

    @POST("/UserWordList/{uid}.json")
    suspend fun postWordList(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: WordListDTO,
    ): Response<PostDTO>

    @PUT("/UserWordList/{uid}/{wordListId}.json")
    suspend fun putWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
        @Body data: WordListDTO,
    ): Response<WordListDTO>

    @DELETE("/UserWordList/{uid}/{wordListId}.json")
    suspend fun deleteWordList(
        @Path("uid") uid: String,
        @Path("wordListId") wordListId: String,
        @Query("auth") token: String,
    ): Response<String>

    @DELETE("/UserWordList/{uid}.json")
    suspend fun deleteAllWordLists(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * WordList
     */
    @GET("/WordList/{uid}/{wordListUid}/words.json")
    suspend fun getWords(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
    ): Response<List<WordDTO>>

    @PUT("/WordList/{uid}/{wordListUid}/words.json")
    suspend fun putWords(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
        @Body data: List<WordDTO>,
    ): Response<List<WordDTO>>

    @POST("/WordList/{uid}/{wordListUid}/words.json")
    suspend fun postWord(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
        @Body data: WordDTO,
    ): Response<PostDTO>

    @DELETE("/WordList/{uid}/{wordListUid}.json")
    suspend fun deleteWords(
        @Path("uid") uid: String,
        @Path("wordListUid") wordListUid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * UserLog
     */
    @GET("/UserLog/{uid}/review.json")
    suspend fun getReviewWords(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<Map<String, List<WordDTO>>>

    @POST("/UserLog/{uid}/review.json")
    suspend fun postReviewWords(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: List<WordDTO>,
    ): Response<PostDTO>

    @PUT("/UserLog/{uid}/review.json")
    suspend fun deleteReviewWords(
        @Path("uid") uid: String,
        @Query("auth") token: String,
        @Body data: List<WordDTO>,
    ): Response<List<WordDTO>>

    @GET("/UserLog/{uid}/{yearMonth}.json")
    suspend fun getTestLog(
        @Path("uid") uid: String,
        @Path("yearMonth") yearMonth: String,
        @Query("auth") token: String,
    ): Response<List<TestResultLogDTO>>

    @POST("/UserLog/{uid}/{yearMonth}.json")
    suspend fun postTestLog(
        @Path("uid") uid: String,
        @Path("yearMonth") yearMonth: String,
        @Query("auth") token: String,
        @Body data: TestResultLogDTO,
    ): Response<PostDTO>

    @DELETE("/UserLog/{uid}.json")
    suspend fun deleteUserLogs(
        @Path("uid") uid: String,
        @Query("auth") token: String,
    ): Response<String>

    /**
     * Community
     */
}
