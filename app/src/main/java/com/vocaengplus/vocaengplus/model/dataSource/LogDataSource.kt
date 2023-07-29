package com.vocaengplus.vocaengplus.model.dataSource

import com.vocaengplus.vocaengplus.model.data.TestResult
import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.data.toTestResultDTO
import com.vocaengplus.vocaengplus.model.data.toTestResultLog
import com.vocaengplus.vocaengplus.model.data.toWord
import com.vocaengplus.vocaengplus.model.data.toWordDto
import com.vocaengplus.vocaengplus.network.dto.RequestUser
import com.vocaengplus.vocaengplus.network.dto.toWord
import com.vocaengplus.vocaengplus.network.service.DatabaseService
import com.vocaengplus.vocaengplus.util.getTodayYearMonth
import javax.inject.Inject

class LogDataSource @Inject constructor(
    private val databaseService: DatabaseService,
) {

    suspend fun saveTestResult(
        requestUser: RequestUser,
        wordListUid: String,
        results: List<TestResult>,
    ): Result<Boolean> {
        val yearMonth = getTodayYearMonth()

        val networkResponse = databaseService.postTestLog(
            requestUser.uid,
            yearMonth,
            requestUser.idToken,
            results.toTestResultLog().toTestResultDTO()
        )

        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                saveReviewWords(requestUser, wordListUid, results)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    private suspend fun saveReviewWords(
        requestUser: RequestUser,
        wordListUid: String,
        words: List<TestResult>,
    ): Result<Boolean> {
        val networkResponse = databaseService.postReviewWords(
            requestUser.uid,
            requestUser.idToken,
            words.map { it.toWord(wordListUid).toWordDto() }
        )

        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getReviewWords(
        requestUser: RequestUser,
    ): Result<List<Word>> {
        val networkResponse = databaseService.getReviewWords(
            requestUser.uid,
            requestUser.idToken
        )
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.values.flatten().map { it2 -> it2.toWord() })
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }
}