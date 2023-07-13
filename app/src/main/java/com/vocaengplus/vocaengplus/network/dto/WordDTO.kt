package com.vocaengplus.vocaengplus.network.dto

import com.google.gson.annotations.SerializedName
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList

data class WordListDTO(
    @SerializedName("wordListName") val wordListName: String,
    @SerializedName("writerUid") val writerUid: String,
    @SerializedName("downloadDate") val downLoadDate: Long,
    @SerializedName("description") val description: String,
)

data class WordDTO(
    @SerializedName("wordListUid") val wordListUid: String,
    @SerializedName("word") val word: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("checked") val checked: Boolean,
)

fun Map.Entry<String, WordListDTO>.toWordList(): WordList {
    return WordList(
        key,
        value.wordListName,
        value.writerUid,
        value.downLoadDate,
        value.description,
    )
}

fun WordListDTO.toWordList(wordListUid: String): WordList {
    return WordList(
        wordListUid,
        wordListName,
        writerUid,
        downLoadDate,
        description
    )
}

fun WordDTO.toWord(): Word {
    return Word(
        wordListUid,
        word,
        meaning,
        checked
    )
}