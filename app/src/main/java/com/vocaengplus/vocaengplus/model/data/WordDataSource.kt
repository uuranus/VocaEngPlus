package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.dto.toWordList
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
        val networkResponse = databaseService.getUserWordList(uid, idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
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
    ): Result<WordList> {
        val networkResponse = databaseService.getWordList(uid, wordListUid, idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.toWordList())
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getWordLists(
        uid: String,
        idToken: String,
    ): Result<List<WordList>> {
        val networkResponse = databaseService.getWordLists(uid, idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.map { it2 -> it2.toWordList() })
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }
}