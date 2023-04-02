package com.vocaengplus.vocaengplus.network.dto

import com.google.gson.annotations.SerializedName

data class UserDataDTO(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("lastLogin") val lastLogin: Long,
    @SerializedName("isGuest") val isGuest: Boolean
)

data class PostDTO(
    @SerializedName("name") val name: String
)