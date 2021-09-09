package com.feragusper.eventbritelite.model

import com.feragusper.eventbritelite.extension.toGMTDate

data class Event(
    val id: String,
    val imageURL: String?,
    val name: String,
    val startUTCDate: String,
    val endUTCDate: String? = null,
    val description: String? = null,
    val currency: String? = null,
    val venue: String? = null,
) {
    val startGMTDate: String = startUTCDate.toGMTDate()
    val endGMTDate: String? = endUTCDate?.toGMTDate()
    val gmtDates: String? = "$startGMTDate - $endGMTDate"
}
