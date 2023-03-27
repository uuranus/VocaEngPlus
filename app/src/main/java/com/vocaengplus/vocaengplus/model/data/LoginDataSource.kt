package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import com.vocaengplus.vocaengplus.model.data.newData.UserData
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.auth.AuthService
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

    suspend fun makeNewUserData(idToken:String,userData: UserData): Response<Boolean> {
        return databaseService.postUserData( userData.uid,idToken,userData)
    }

}