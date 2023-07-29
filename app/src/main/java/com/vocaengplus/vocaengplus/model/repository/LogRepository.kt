package com.vocaengplus.vocaengplus.model.repository

import com.vocaengplus.vocaengplus.model.data.TestResult
import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.dataSource.LogDataSource
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.RequestUser
import javax.inject.Inject

class LogRepository @Inject constructor(
    private val dataSource: LogDataSource,
) {
    private suspend fun getRequestUser(): RequestUser {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return RequestUser(uid, idToken)
    }

    suspend fun saveTestResult(wordListUid: String, results: List<TestResult>): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.saveTestResult(requestUser, wordListUid, results)
    }

    suspend fun getReviews(): Result<List<Word>> {
        val requestUser = getRequestUser()

        return dataSource.getReviewWords(requestUser)
    }

}