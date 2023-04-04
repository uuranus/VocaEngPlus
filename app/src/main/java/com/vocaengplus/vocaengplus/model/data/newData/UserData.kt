package com.vocaengplus.vocaengplus.model.data.newData

import com.vocaengplus.vocaengplus.network.dto.UserDataDTO


data class UserData(
    val nickname: String,
    val uid: String,
    val lastLogin: Long,
    val isGuest: Boolean
)

fun UserData.toUserDataDto(): UserDataDTO {
    return UserDataDTO(
        nickname = nickname,
        uid = uid,
        lastLogin = lastLogin,
        isGuest = isGuest
    )
}
