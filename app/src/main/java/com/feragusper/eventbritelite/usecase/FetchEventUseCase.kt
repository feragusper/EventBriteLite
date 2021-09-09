package com.feragusper.eventbritelite.usecase

import com.feragusper.eventbritelite.data.EventRepository
import com.feragusper.eventbritelite.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchEventUseCase @Inject constructor(private val repository: EventRepository) {

    suspend fun invoke(eventId: String): Flow<Event> = repository.fetchEvent(eventId = eventId)

}