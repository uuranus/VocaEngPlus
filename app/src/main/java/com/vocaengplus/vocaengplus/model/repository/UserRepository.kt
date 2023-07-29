package com.vocaengplus.vocaengplus.model.repository

import android.net.Uri
import com.vocaengplus.vocaengplus.model.data.User
import com.vocaengplus.vocaengplus.model.data.UserAuth
import com.vocaengplus.vocaengplus.network.auth.AuthService

class UserRepository {

    fun getUserName(): String {
        return AuthService.getCurrentUserInfo()?.name ?: ""
    }

    fun getUserPhotoUrl(): String {
        return AuthService.getCurrentUserInfo()?.emailId ?: ""
    }

    fun getUserInfo(): UserAuth? {
        return AuthService.getCurrentUserInfo()
    }

    suspend fun saveNewProfile(imageUri: Uri?, nickname: String) {
        AuthService.setProfile(imageUri, nickname)
    }
}