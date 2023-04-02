package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.model.data.LoginDataSource
import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.model.data.newData.toUserDataDto
import com.vocaengplus.vocaengplus.network.auth.AuthService
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

        val response = dataSource.makeNewUserData(idToken, userData.toUserDataDto())
        return if (response.isSuccessful) {
            Result.success(true)
        } else {
            Result.failure(Exception("로그인에 실패했습니다"))
        }
    }

}