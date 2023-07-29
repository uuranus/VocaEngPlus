package com.vocaengplus.vocaengplus.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private val calendar: Calendar = Calendar.getInstance()
private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

fun getToday(): String {
    return String.format(
        "%04d-%02d-%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DATE)
    )
}

suspend fun getTodayYearMonth(): String {
    return String.format(
        "%04d%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
    )
}

fun Long.toDateString(): String {
    val date = Date(this)
    return dateFormat.format(date)
}