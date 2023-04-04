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

data class WordListDTO(
    @SerializedName("wordListName") val wordListName: String,
    @SerializedName("wordListWriter") val wordListWriter: String,
    @SerializedName("wordListWriterToken") val wordListWriterToken: String,
    @SerializedName("downloadDate") val downLoadDate: Long,
    @SerializedName("description") val description: String,
    @SerializedName("words") val words: List<WordDTO>
)

data class WordDTO(
    @SerializedName("word") val word: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("checked") val checked: Boolean,
    @SerializedName("wordListName") val wordListName: String
)