package com.vocaengplus.vocaengplus.model.repository

import com.vocaengplus.vocaengplus.model.data.User
import com.vocaengplus.vocaengplus.model.data.UserAuth
import com.vocaengplus.vocaengplus.model.data.toUserDto
import com.vocaengplus.vocaengplus.model.data.toWordDto
import com.vocaengplus.vocaengplus.model.data.toWordListDto
import com.vocaengplus.vocaengplus.model.dataSource.LoginDataSource
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.util.WORDLIST
import com.vocaengplus.vocaengplus.util.makeDefaultWordList
import com.vocaengplus.vocaengplus.util.makeDefaultWords
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource,
) {

    suspend fun requestLogin(email: String, password: String): Result<UserAuth> {
        return dataSource.login(email, password)
    }

    suspend fun requestGuestLogin(): Result<UserAuth> {
        return dataSource.loginAsGuest()
    }

    suspend fun requestMakeNewUserData(userData: User): Result<Boolean> {
        val idToken = AuthService.getCurrentUserIdToken()

        println("token ${idToken}")
        if (idToken.isEmpty()) return Result.failure(Exception("로그인이 되어있지 않습니다"))

        val userDataResponse = dataSource.makeNewUserData(idToken, userData.toUserDto())
        var wordDataResponse = true
        for (type in WORDLIST.values()) {
            val defaultWordList =
                makeDefaultWordList(type, userData.nickname, userData.uid).toWordListDto()

            val wordListId = dataSource.makeDefaultWordList(
                idToken, userData.uid,
                defaultWordList
            )
            if (wordListId.isSuccessful) {
                wordListId.body()?.let {
                    val defaultWords = makeDefaultWords(type, it.name)
                    val userWordListResponse = dataSource.makeDefaultWords(
                        idToken,
                        userData.uid,
                        it.name,
                        defaultWords.map { it.toWordDto() }
                    )

                    if (userWordListResponse.isSuccess.not()) {
                        wordDataResponse = false
                    }
                }
            }
        }

        return if (userDataResponse.isSuccessful && wordDataResponse) {
            Result.success(true)
        } else {
            Result.failure(Exception("회원가입에 실패했습니다"))
        }
    }

    suspend fun logOut(): Result<Boolean> {
        return dataSource.logOut()
    }

    suspend fun requestRegister(email: String, password: String): Result<UserAuth> {
        return dataSource.register(email, password)
    }

    suspend fun findPassword(email: String) {
        dataSource.setNewPassword(email)
    }

    suspend fun quit(): Result<Boolean> {

        val idToken = AuthService.getCurrentUserIdToken()
        val uid = AuthService.getCurrentUID()

        return dataSource.quit(uid, idToken)
    }

}