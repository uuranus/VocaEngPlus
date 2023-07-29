package com.vocaengplus.vocaengplus.model.dataSource

import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.data.WordList
import com.vocaengplus.vocaengplus.model.data.toWordDto
import com.vocaengplus.vocaengplus.model.data.toWordListDto
import com.vocaengplus.vocaengplus.network.dto.RequestUser
import com.vocaengplus.vocaengplus.network.dto.toWord
import com.vocaengplus.vocaengplus.network.dto.toWordList
import com.vocaengplus.vocaengplus.network.service.DatabaseService
import javax.inject.Inject

class WordDataSource @Inject constructor(
    private val databaseService: DatabaseService,
) {

    suspend fun addWord(
        requestUser: RequestUser,
        wordListUid: String,
        words: List<Word>,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.putWords(
                requestUser.uid,
                wordListUid,
                requestUser.idToken,
                words.map { it.toWordDto() })
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun addOneWord(
        requestUser: RequestUser,
        wordListUid: String,
        word: Word,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.postWord(
                requestUser.uid,
                wordListUid,
                requestUser.idToken,
                word.toWordDto()
            )
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun editWord(
        requestUser: RequestUser,
        wordListUid: String,
        words: List<Word>,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.putWords(
                requestUser.uid,
                wordListUid,
                requestUser.idToken,
                words.map { it.toWordDto() })
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun deleteWord(
        requestUser: RequestUser,
        wordListUid: String,
        words: List<Word>,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.putWords(
                requestUser.uid,
                wordListUid,
                requestUser.idToken,
                words.map { it.toWordDto() })
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getWordLists(
        requestUser: RequestUser,
    ): Result<List<WordList>> {
        val networkResponse = databaseService.getWordLists(requestUser.uid, requestUser.idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.entries.map { it2 -> it2.toWordList() })
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getWordList(
        requestUser: RequestUser,
        wordListUid: String,
    ): Result<WordList> {
        val networkResponse =
            databaseService.getWordList(requestUser.uid, wordListUid, requestUser.idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.toWordList(wordListUid))
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun addWordList(
        requestUser: RequestUser,
        newWordList: WordList,
        newWords: List<Word>,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.postWordList(
                requestUser.uid,
                requestUser.idToken,
                newWordList.copy(writerUid = requestUser.uid).toWordListDto()
            )
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                if (addWords(
                        requestUser,
                        it.name,
                        newWords.map { it2 -> it2.copy(wordListUid = it.name) }
                    ).isSuccess
                ) {
                    Result.success(true)
                } else {
                    Result.failure(Exception())
                }
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun editWordList(
        requestUser: RequestUser,
        wordList: WordList,
    ): Result<WordList> {
        val networkResponse =
            databaseService.putWordList(
                requestUser.uid,
                wordList.wordListUid,
                requestUser.idToken,
                wordList.toWordListDto()
            )
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.toWordList(wordList.wordListUid))
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun deleteWordList(
        requestUser: RequestUser,
        wordListUid: String,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.deleteWordList(requestUser.uid, wordListUid, requestUser.idToken)
        return if (networkResponse.isSuccessful) {
            val deleteResponse = deleteWords(requestUser, wordListUid)
            if (deleteResponse.isSuccess) {
                Result.success(true)
            } else {
                Result.failure(Exception())
            }
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getWords(requestUser: RequestUser, wordListUid: String): Result<List<Word>> {
        val networkResponse =
            databaseService.getWords(requestUser.uid, wordListUid, requestUser.idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.map { it2 -> it2.toWord() })

            } ?: Result.failure(Exception())

        } else {
            Result.failure(Exception())
        }
    }

    private suspend fun addWords(
        requestUser: RequestUser,
        wordListUid: String,
        words: List<Word>,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.putWords(
                requestUser.uid,
                wordListUid,
                requestUser.idToken,
                words.map { it.toWordDto() })
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    private suspend fun deleteWords(
        requestUser: RequestUser,
        wordListUid: String,
    ): Result<Boolean> {
        val networkResponse =
            databaseService.deleteWords(
                requestUser.uid,
                wordListUid,
                requestUser.idToken
            )
        return if (networkResponse.isSuccessful) {
            Result.success(true)
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun getMyWords(requestUser: RequestUser, wordListUid: String): Result<List<Word>> {
        val networkResponse =
            databaseService.getWords(requestUser.uid, wordListUid, requestUser.idToken)
        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(it.filter { it2 -> it2.checked }.map { it3 -> it3.toWord() })
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }
}