package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.newData.WordListUidName
import com.vocaengplus.vocaengplus.model.data.newData.toWordListDto
import com.vocaengplus.vocaengplus.model.data.newData.toWordListUidName
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
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
    ): Result<List<WordListUidName>> {
        val networkResponse = databaseService.getUserWordList(uid, idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.entries.map { it2 -> it2.toWordListUidName() })
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
                Result.success(it.values.map { it2 -> it2.toWordList() })
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun addNewWordList(
        newWordList: WordList,
        uid: String,
        idToken: String,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.postWordList(uid, idToken, newWordList.toWordListDto())
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                if (addNewUserWordList(uid, idToken, it.name, newWordList.wordListName).isSuccess) {
                    Result.success(true)
                } else {
                    Result.failure(Exception())
                }
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    private suspend fun addNewUserWordList(
        uid: String,
        idToken: String,
        wordListUid: String,
        wordListName: String,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.putUserWordList(uid, wordListUid, idToken, wordListName)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }
}