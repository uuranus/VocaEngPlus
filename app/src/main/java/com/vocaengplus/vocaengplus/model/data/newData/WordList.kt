package com.vocaengplus.vocaengplus.model.data.newData

import com.vocaengplus.vocaengplus.network.dto.WordDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO

data class WordList(
    val wordListName: String = "",
    val wordListWriter: String = "",
    val wordListWriterToken: String = "",
    val downLoadDate: Long = 0L,
    val description: String = "",
    val words: List<Word> = emptyList(),
)

data class Word(
    val word: String,
    val meaning: String,
    val checked: Boolean,
    val wordListName: String,
)

data class WordListUidName(
    val uid: String,
    val name: String,
)

fun WordList.toWordListDto(): WordListDTO {
    return WordListDTO(
        wordListName,
        wordListWriter,
        wordListWriterToken,
        downLoadDate,
        description,
        words.map { it.toWordDto() }
    )
}

fun Word.toWordDto(): WordDTO {
    return WordDTO(
        word,
        meaning,
        checked,
        wordListName
    )
}

fun Map.Entry<String, String>.toWordListUidName(): WordListUidName {
    return WordListUidName(key, value)
}