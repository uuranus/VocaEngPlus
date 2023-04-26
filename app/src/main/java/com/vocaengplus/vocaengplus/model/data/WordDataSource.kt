package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import javax.inject.Inject

class WordDataSource @Inject constructor(
    private val databaseService: DatabaseService,
) {

    suspend fun saveNewWord(voca: Voca): Result<Voca> {
        return Result.failure(Exception())
    }

    suspend fun saveEditedVoca(voca: Voca): Result<Voca> {
        return Result.failure(Exception())
    }

    suspend fun getWordListData(
        wordListUid: String,
        uid: String,
        idToken: String,
    ): Result<WordListDTO> {
        val response = databaseService.getWordList(uid, idToken, wordListUid)
        return if (response.isSuccessful) {
            Result.success(response.body() ?: WordListDTO("", "", "", 0, "", emptyList()))
        } else {
            Result.failure(Exception())
        }
    }
}