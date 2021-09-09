package com.feragusper.eventbritelite.data

import androidx.paging.*
import com.feragusper.eventbritelite.data.api.EventAPIService
import com.feragusper.eventbritelite.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

private const val STARTING_PAGE_INDEX = 1
private const val PAGE_SIZE = 50

/**
 * Repository module for handling data operations.
 */
@Singleton
class EventRepository @Inject constructor(
    private val eventAPIService: EventAPIService,
) {

    fun fetchEvents(): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                EventsPagingSource(dataSource = eventAPIService)
            }
        ).flow
    }

    suspend fun fetchEvent(eventId: String): Flow<Event> {
        return flow {
            emit(eventAPIService.fetchEvent(eventId).toEvent())
        }.flowOn(Dispatchers.IO)
    }

}

class EventsPagingSource(val dataSource: EventAPIService) : PagingSource<Int, Event>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = dataSource.getEventsByOrganization(
                page = page,
                pageSize = PAGE_SIZE
            )
            LoadResult.Page(
                data = response.toEvents(),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.pagination.pageCount) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
