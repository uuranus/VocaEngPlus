package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.network.dto.TestResultLogDTO

data class TestResultLog(
    val total: Int,
    val correct: Int,
    val inCorrect: Int,
)

fun TestResultLog.toTestResultDTO(): TestResultLogDTO {
    return TestResultLogDTO(
        total,
        correct,
        inCorrect
    )
}