package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDataDTO
import retrofit2.Response
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val authService: AuthService,
    private val databaseService: DatabaseService
) {

    suspend fun login(email: String, password: String): Result<UserAuth> {
        return authService.login(email, password)
    }

    suspend fun loginAsGuest(): Result<UserAuth> {
        return authService.loginAsGuest()
    }

    suspend fun makeNewUserData(idToken: String, userData: UserDataDTO): Response<UserDataDTO> {
        return databaseService.putUserData(userData.uid, idToken, userData)
    }

    suspend fun register(email: String, password: String): Result<UserAuth> {
        return authService.register(email, password)
    }

}