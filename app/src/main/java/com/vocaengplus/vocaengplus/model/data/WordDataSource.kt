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

    suspend fun getWordListNames(
        uid: String,
        idToken: String,
    ): Result<List<String>> {
        val response = databaseService.getUserWordList(uid, idToken)
        return if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it.keys.toList())
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getWordList(
        wordListUid: String,
        uid: String,
        idToken: String,
    ): Result<WordListDTO> {
        val response = databaseService.getWordList(uid, wordListUid, idToken)
        return if (response.isSuccessful) {
            Result.success(response.body() ?: WordListDTO("", "", "", 0, "", emptyList()))
        } else {
            Result.failure(Exception())
        }
    }
}