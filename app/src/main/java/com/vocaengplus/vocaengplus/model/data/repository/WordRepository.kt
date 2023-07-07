package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.model.data.WordDataSource
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.newData.WordListUidName
import com.vocaengplus.vocaengplus.network.auth.AuthService
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

    suspend fun getWordListNames(): Result<List<WordListUidName>> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return dataSource.getWordListNames(uid, idToken)
    }

    suspend fun getWordList(wordListUid: String): Result<WordList> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return dataSource.getWordList(wordListUid, uid, idToken)
    }

    suspend fun setMyWord(voca: Voca): Result<Voca> {
        return Result.failure(Exception())
    }

    suspend fun getWordLists(): Result<List<WordList>> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return dataSource.getWordLists(uid, idToken)
    }

    suspend fun addNewWordList(newWordList: WordList): Result<Boolean> {
        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()
        val userName = AuthService.getCurrentUserInfo()?.name ?: ""

        return dataSource.addNewWordList(
            newWordList.copy(
                wordListWriter = userName,
                wordListWriterToken = uid
            ),
            uid,
            idToken
        )
    }
}