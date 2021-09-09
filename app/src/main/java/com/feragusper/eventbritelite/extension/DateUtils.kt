package com.feragusper.eventbritelite.extension

import java.text.SimpleDateFormat
import java.util.*

private const val ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun String.toGMTDate(): String {
    val utcFormatter = SimpleDateFormat(ISO8601_FORMAT, Locale.getDefault())
    utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
    val gmtFormatter = SimpleDateFormat("E, MMM dd HH:mm'hs'", Locale.getDefault())
    gmtFormatter.timeZone = TimeZone.getDefault()

    return utcFormatter.parse(this)?.let { utcDate ->
        gmtFormatter.format(utcDate)
    } ?: ""
}