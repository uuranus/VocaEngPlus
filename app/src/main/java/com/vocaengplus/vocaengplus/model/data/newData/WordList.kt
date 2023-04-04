package com.vocaengplus.vocaengplus.model.data.newData

import com.vocaengplus.vocaengplus.network.dto.WordDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO

data class WordList(
    val wordListName: String,
    val wordListWriter: String,
    val wordListWriterToken: String,
    val downLoadDate: Long,
    val description: String,
    val words: List<Word>
)

data class Word(
    val word: String,
    val meaning: String,
    val checked: Boolean,
    val wordListName: String
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