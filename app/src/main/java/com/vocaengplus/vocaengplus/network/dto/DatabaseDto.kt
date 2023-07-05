package com.vocaengplus.vocaengplus.network.dto

import com.google.gson.annotations.SerializedName
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList

data class UserDTO(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("lastLogin") val lastLogin: Long,
    @SerializedName("isGuest") val isGuest: Boolean,
)

data class PostDTO(
    @SerializedName("name") val name: String,
)

data class WordListDTO(
    @SerializedName("wordListName") val wordListName: String,
    @SerializedName("wordListWriter") val wordListWriter: String,
    @SerializedName("wordListWriterToken") val wordListWriterToken: String,
    @SerializedName("downloadDate") val downLoadDate: Long,
    @SerializedName("description") val description: String,
    @SerializedName("words") val words: List<WordDTO>,
)

data class WordDTO(
    @SerializedName("word") val word: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("checked") val checked: Boolean,
    @SerializedName("wordListName") val wordListName: String,
)

fun WordListDTO.toWordList(): WordList {
    return WordList(
        wordListName,
        wordListWriter,
        wordListWriterToken,
        downLoadDate,
        description,
        words.map { it.toWord() }
    )
}

fun WordDTO.toWord(): Word {
    return Word(
        word,
        meaning,
        checked,
        wordListName
    )
}