package com.vocaengplus.vocaengplus.model.data.newData

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
    val wordList: String
)