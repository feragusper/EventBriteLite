package com.feragusper.eventbritelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feragusper.eventbritelite.data.EventRepository
import com.feragusper.eventbritelite.model.Event
import com.feragusper.eventbritelite.state.Resource
import com.feragusper.eventbritelite.usecase.FetchEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject internal constructor(
    private val fetchEventUseCase: FetchEventUseCase,
) : ViewModel() {

    private val _fetchEventFlow = Channel<Resource<Event>>(Channel.BUFFERED)
    val fetchEventFlow = _fetchEventFlow.receiveAsFlow()

    fun fetchEvent(eventId: String) {
        viewModelScope.launch {
            _fetchEventFlow.send(Resource.loading())
            fetchEventUseCase.invoke(eventId)
                .catch { e ->
                    _fetchEventFlow.send(Resource.error(e.toString()))
                }
                .collect { event ->
                    _fetchEventFlow.send(Resource.success(event))
                }
        }
    }
}
