package com.vocaengplus.vocaengplus.model.data.newData

import com.vocaengplus.vocaengplus.network.dto.UserDTO


data class User(
    val nickname: String,
    val uid: String,
    val lastLogin: Long,
    val isGuest: Boolean,
)

fun User.toUserDto(): UserDTO {
    return UserDTO(
        nickname = nickname,
        uid = uid,
        lastLogin = lastLogin,
        isGuest = isGuest
    )
}
