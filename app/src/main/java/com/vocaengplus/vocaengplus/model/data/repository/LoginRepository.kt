package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.model.data.LoginDataSource
import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.toUserDataDto
import com.vocaengplus.vocaengplus.model.data.newData.toWordListDto
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.UserDataDTO
import com.vocaengplus.vocaengplus.ui.util.WORDLIST
import com.vocaengplus.vocaengplus.ui.util.makeDefaultWordList
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
) {

    suspend fun requestLogin(email: String, password: String): Result<UserAuth> {
        return dataSource.login(email, password)
    }

    suspend fun requestGuestLogin(): Result<UserAuth> {
        return dataSource.loginAsGuest()
    }

    suspend fun requestMakeNewUserData(userData: UserData): Result<Boolean> {
        val idToken = AuthService.getCurrentUserIdToken()

        println("token ${idToken}")
        if (idToken.isEmpty()) return Result.failure(Exception("로그인이 되어있지 않습니다"))

        val userDataResponse = dataSource.makeNewUserData(idToken, userData.toUserDataDto())
        var wordDataResponse = true
        for (type in WORDLIST.values()) {
            val wordListId = dataSource.makeDefaultWordList(
                idToken, userData.uid,
                makeDefaultWordList(type, userData.nickname, userData.uid).toWordListDto()
            )
            if (wordListId.isSuccessful) {
                wordListId.body()?.let {
                    val userWordListResponse = dataSource.makeUserWordList(
                        idToken,
                        userData.uid,
                        it.name
                    )

                    if (userWordListResponse.isSuccessful.not()) {
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

    suspend fun requestRegister(email: String, password: String): Result<UserAuth> {
        return dataSource.register(email, password)
    }

}