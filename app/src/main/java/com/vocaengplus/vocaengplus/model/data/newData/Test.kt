package com.vocaengplus.vocaengplus.model.data.newData

data class Test(
    val question: String,
    val answers: List<String>,
    val answer: String,
)

data class TestResult(
    val question: String,
    val answer: String,
    val isCorrect:Boolean
)