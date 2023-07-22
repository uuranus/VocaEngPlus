package com.vocaengplus.vocaengplus.network.dto

import com.google.gson.annotations.SerializedName

data class TestResultLogDTO(
    @SerializedName("total") val total: Int,
    @SerializedName("correct") val correct: Int,
    @SerializedName("inCorrect") val inCorrect: Int,
)
