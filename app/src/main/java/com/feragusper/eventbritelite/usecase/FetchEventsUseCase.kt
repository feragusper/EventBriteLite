package com.feragusper.eventbritelite.usecase

import androidx.paging.PagingData
import com.feragusper.eventbritelite.data.EventRepository
import com.feragusper.eventbritelite.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchEventsUseCase @Inject constructor(private val repository: EventRepository) {
    operator fun invoke(): Flow<PagingData<Event>> = repository.fetchEvents()
}