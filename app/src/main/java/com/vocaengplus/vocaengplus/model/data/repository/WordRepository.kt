package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.model.data.dataSource.WordDataSource
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.RequestUser
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val dataSource: WordDataSource,
) {

    private suspend fun getRequestUser(): RequestUser {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return RequestUser(uid, idToken)
    }

    suspend fun addWord(wordListUid: String, words: List<Word>): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.addWord(requestUser, wordListUid, words)
    }

    suspend fun addOneWord(wordListUid: String, word: Word): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.addOneWord(requestUser, wordListUid, word)
    }

    suspend fun editWord(wordListUid: String, words: List<Word>): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.editWord(requestUser, wordListUid, words)
    }

    suspend fun deleteWord(wordListUid: String, words: List<Word>): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.deleteWord(requestUser, wordListUid, words)
    }


    suspend fun getWordLists(): Result<List<WordList>> {
        val requestUser = getRequestUser()
        val userName = AuthService.getCurrentUserInfo()?.name ?: ""

        return dataSource.getWordLists(requestUser).map {
            it.map { it2 -> it2.copy(writerName = userName) }
        }
    }

    suspend fun getWordList(wordListUid: String): Result<WordList> {
        val requestUser = getRequestUser()

        return dataSource.getWordList(requestUser, wordListUid)
    }

    suspend fun addWordList(wordList: WordList, words: List<Word>): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.addWordList(
            requestUser,
            wordList,
            words
        )
    }

    suspend fun editWordList(wordList: WordList): Result<WordList> {
        val requestUser = getRequestUser()

        return dataSource.editWordList(
            requestUser,
            wordList
        )
    }

    suspend fun deleteWordList(wordListUid: String): Result<Boolean> {
        val requestUser = getRequestUser()

        return dataSource.deleteWordList(
            requestUser,
            wordListUid
        )
    }

    suspend fun getWords(wordListUid: String): Result<List<Word>> {
        val requestUser = getRequestUser()

        return dataSource.getWords(
            requestUser,
            wordListUid
        )
    }

    suspend fun getMyWords(wordListUid: String): Result<List<Word>> {
        val requestUser = getRequestUser()

        return dataSource.getMyWords(
            requestUser,
            wordListUid
        )
    }
}