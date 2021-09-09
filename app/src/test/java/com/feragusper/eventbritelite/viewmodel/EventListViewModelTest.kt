package com.feragusper.eventbritelite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.turbine.test
import com.feragusper.eventbritelite.common.MainCoroutineRule
import com.feragusper.eventbritelite.data.EventsPagingSource
import com.feragusper.eventbritelite.data.api.EventAPIService
import com.feragusper.eventbritelite.state.Resource
import com.feragusper.eventbritelite.usecase.FetchEventsUseCase
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

/**
 * Unit tests for the implementation of [EventListViewModel].
 */
@RunWith(MockitoJUnitRunner::class)
class EventListViewModelTest {

    private lateinit var eventListViewModel: EventListViewModel

    @Mock
    private lateinit var fetchEventsUseCase: FetchEventsUseCase

    @Mock
    private lateinit var eventAPIService: EventAPIService

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setupViewModel() {
        // Create class under test
        eventListViewModel = EventListViewModel(fetchEventsUseCase)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun fetchEventsSuccess() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(fetchEventsUseCase()).willReturn(Pager(
                config = PagingConfig(
                    enablePlaceholders = true,
                    pageSize = 50
                ),
                pagingSourceFactory = {
                    EventsPagingSource(dataSource = eventAPIService)
                }
            ).flow)

            eventListViewModel.fetchEvents()

            eventListViewModel.fetchEventsFlow.test {
                Truth.assertThat(awaitItem().status).isEqualTo(Resource.Status.LOADING)
                Truth.assertThat(awaitItem().status).isEqualTo(Resource.Status.SUCCESS)
            }
        }

    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun fetchEventsError() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(fetchEventsUseCase()).willReturn(
                flow {
                    throw RuntimeException()
                }
            )

            eventListViewModel.fetchEvents()

            eventListViewModel.fetchEventsFlow.test {
                Truth.assertThat(awaitItem().status).isEqualTo(Resource.Status.LOADING)
                Truth.assertThat(awaitItem().status).isEqualTo(Resource.Status.ERROR)
            }
        }

    }

}
