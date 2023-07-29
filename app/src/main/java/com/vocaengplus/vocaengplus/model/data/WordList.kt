package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.network.dto.WordDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO

data class WordList(
    val wordListUid: String = "",
    val wordListName: String = "",
    val writerUid: String = "",
    val writerName: String = "",
    val downLoadDate: Long = 0L,
    val description: String = "",
)

data class Word(
    val wordListUid: String,
    val word: String,
    val meaning: String,
    val checked: Boolean = false,
)

fun WordList.toWordListDto(): WordListDTO {
    return WordListDTO(
        wordListName,
        writerUid,
        downLoadDate,
        description,
    )
}

fun Word.toWordDto(): WordDTO {
    return WordDTO(
        wordListUid,
        word,
        meaning,
        checked
    )
}
