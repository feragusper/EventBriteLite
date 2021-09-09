package com.feragusper.eventbritelite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.feragusper.eventbritelite.adapter.EventPagingDataAdapter
import com.feragusper.eventbritelite.databinding.FragmentEventListBinding
import com.feragusper.eventbritelite.extension.handleError
import com.feragusper.eventbritelite.state.Resource
import com.feragusper.eventbritelite.viewmodel.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventListFragment : Fragment() {

    private val viewModel: EventListViewModel by viewModels()
    private val adapter = EventPagingDataAdapter { event ->
        findNavController().navigate(EventListFragmentDirections.eventListFragmentDestToEventDetailFragmentDest(event.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentEventListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.eventList.adapter = adapter
        binding.eventList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        subscribeUi(adapter, binding.loading)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchEvents()
    }


    private fun subscribeUi(adapter: EventPagingDataAdapter, loading: View) {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        loading.isVisible = false
                        handleError((loadStates.refresh as LoadState.Error).error.message)
                    }
                    is LoadState.NotLoading -> {
                        loading.isVisible = false
                    }
                    LoadState.Loading -> {
                        loading.isVisible = true
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.fetchEventsFlow.collectLatest { fetchResource ->
                when (fetchResource.status) {
                    Resource.Status.SUCCESS -> {
                        fetchResource.data?.let { tvShowPagingData ->
                            adapter.submitData(tvShowPagingData)
                        }
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, fetchResource.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.FAILURE -> {
                        Toast.makeText(context, fetchResource.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

}
