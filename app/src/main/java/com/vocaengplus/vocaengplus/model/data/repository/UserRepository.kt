package com.vocaengplus.vocaengplus.model.data.repository

import com.vocaengplus.vocaengplus.network.auth.AuthService

class UserRepository {

    fun getUserName(): String {
        return AuthService.getCurrentUserInfo()?.name ?: ""
    }


}