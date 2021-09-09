package com.feragusper.eventbritelite.data.api

import com.feragusper.eventbritelite.model.Event
import com.google.gson.annotations.SerializedName

data class EventListResponse(
    val events: List<EventEntity>,
    val pagination: PaginationEntity,
) {
    fun toEvents() = events.map { tvShowEntity -> tvShowEntity.toEvent() }
}

data class EventEntity(
    private val id: String,
    private val logo: LogoEntity?,
    private val name: RichTextEntity,
    private val start: DateEntity,
    private val end: DateEntity,
    private val description: RichTextEntity,
    private val venue: VenueEntity?,
    private val currency: String,
    @field: SerializedName("is_free") val isFree: Boolean,
) {

    fun toEvent() = Event(
        id = id,
        imageURL = logo?.url,
        name = name.text,
        description = description.text,
        startUTCDate = start.utc,
        endUTCDate = end.utc,
        venue = venue?.name,
        currency = if (isFree) "FREE" else currency,
    )
}

data class PaginationEntity(
    @field: SerializedName("page_number") val pageNumber: Int,
    @field: SerializedName("page_count") val pageCount: Int,
    @field: SerializedName("page_size") val pageSize: Int,
)

data class RichTextEntity(val text: String)

data class LogoEntity(
    val url: String,
)

data class DateEntity(val utc: String)

data class VenueEntity(val name: String)