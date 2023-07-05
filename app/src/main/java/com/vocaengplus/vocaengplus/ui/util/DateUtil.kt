package com.vocaengplus.vocaengplus.ui.util

import java.util.Calendar


val calendar = Calendar.getInstance()

fun getToday(): String {
    return String.format(
        "%04d-%02d-%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DATE)
    )
}