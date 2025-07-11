package com.example.fingid.core.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


private const val INPUT_DATE_FORMAT = "yyyy-MM-dd"
private const val OUTPUT_DATE_FORMAT = "d MMMM yyyy"
private const val OUTPUT_DATE_TIME_FORMAT = "d MMMM HH:mm"
private const val OUTPUT_TIME_FORMAT = "HH:mm"


fun getCurrentDate(): String {
    val sdf = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

    return sdf.format(Date())
}


fun getStartOfCurrentMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}


fun getCurrentDateIso(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return sdf.format(calendar.time)
}


fun formatLongToHumanDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

    return formatter.format(Date(timestamp))
}


fun formatHumanDateToIso(humanDate: String): String {
    return try {
        val humanFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))
        val date = humanFormatter.parse(humanDate)
        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)

        date?.let { isoFormatter.format(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}


fun formatIsoDateToHuman(isoDate: String): String {
    return try {
        val isoFormatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US)
        val date = isoFormatter.parse(isoDate)
        val humanFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale("ru"))

        date?.let { humanFormatter.format(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun getCurrentTime(): String {
    return LocalTime.now().format(DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))
}


fun formatDateAndTime(time: LocalTime, date: LocalDate): String {
    val formatter = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT, Locale("ru"))
    val combinedDate = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant())

    return formatter.format(combinedDate)
}

fun combineDateTimeToIso(dateStr: String, timeStr: String): String {
    val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
    val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))

    return date.atTime(time)
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
}

fun LocalDate.toHumanDate(): String =
    format(DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT, Locale("ru")))

fun LocalTime.toHumanTime(): String =
    format(DateTimeFormatter.ofPattern(OUTPUT_TIME_FORMAT))