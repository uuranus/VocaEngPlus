package com.vocaengplus.vocaengplus.model.data

data class Test(
    val question: String,
    val answers: List<String>,
    val answer: String,
)

data class TestResult(
    val question: String,
    val answer: String,
    val isCorrect: Boolean,
)

fun List<TestResult>.toTestResultLog(): TestResultLog {
    return TestResultLog(
        size,
        count { it.isCorrect },
        count { it.isCorrect.not() }
    )
}

fun TestResult.reverseAnswerQuestion(): TestResult {
    return TestResult(
        answer,
        question,
        isCorrect
    )
}

fun TestResult.toWord(wordListUid: String): Word {
    return Word(
        wordListUid,
        question,
        answer
    )
}