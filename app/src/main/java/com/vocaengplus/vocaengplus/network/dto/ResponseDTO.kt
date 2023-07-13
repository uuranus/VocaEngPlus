package com.vocaengplus.vocaengplus.network.dto

import com.google.gson.annotations.SerializedName

data class PostDTO(
    @SerializedName("name") val name: String,
)

data class WordsDTO(
    @SerializedName("words") val words: List<WordDTO>,
)