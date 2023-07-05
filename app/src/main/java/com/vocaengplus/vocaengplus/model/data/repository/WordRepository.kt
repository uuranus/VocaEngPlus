package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.model.data.WordDataSource
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.toWordList
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val dataSource: WordDataSource,
) {

    suspend fun saveNewWord(voca: Voca) {
        val idToken = AuthService.getCurrentUserIdToken()

        dataSource.saveNewWord(voca)
    }

    suspend fun saveEditedVoca(voca: Voca) {

        val idToken = AuthService.getCurrentUserIdToken()

        dataSource.saveEditedVoca(voca)
    }

    suspend fun getWordListNames():Result<List<String>> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return dataSource.getWordListNames(uid, idToken)
    }

    suspend fun getWordList(wordListUid: String): Result<WordList> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        val result = dataSource.getWordList(wordListUid, uid, idToken)
        return if (result.isSuccess) {
            result.getOrNull()?.let {
                Result.success(it.toWordList())
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun setMyWord(voca: Voca): Result<Voca> {
        return Result.failure(Exception())
    }
}