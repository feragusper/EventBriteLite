package com.feragusper.eventbritelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.feragusper.eventbritelite.model.Event
import com.feragusper.eventbritelite.state.Resource
import com.feragusper.eventbritelite.usecase.FetchEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject internal constructor(
    private val fetchEventsUseCase: FetchEventsUseCase,
) : ViewModel() {

    private val _fetchEventsFlow = Channel<Resource<PagingData<Event>>>(Channel.BUFFERED)
    val fetchEventsFlow = _fetchEventsFlow.receiveAsFlow()

    fun fetchEvents() {
        viewModelScope.launch {
            _fetchEventsFlow.send(Resource.loading())
            fetchEventsUseCase()
                .cachedIn(viewModelScope)
                .catch { e ->
                    _fetchEventsFlow.send(Resource.error(e.toString()))
                }
                .collect { eventPagingData ->
                    _fetchEventsFlow.send(Resource.success(eventPagingData))
                }
        }
    }
}
