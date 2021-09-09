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
import androidx.navigation.fragment.navArgs
import com.feragusper.eventbritelite.databinding.FragmentEventDetailBinding
import com.feragusper.eventbritelite.state.Resource
import com.feragusper.eventbritelite.viewmodel.EventDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val viewModel: EventDetailViewModel by viewModels()

    private val args: EventDetailFragmentArgs by navArgs()

    private val eventId by lazy { args.eventId.sanitize() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        subscribeUi(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchEvent(eventId)
    }

    private fun subscribeUi(binding: FragmentEventDetailBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.fetchEventFlow.collectLatest { fetchResource ->
                when (fetchResource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.loading.isVisible = false
                        binding.event = fetchResource.data
                    }
                    Resource.Status.ERROR -> {
                        binding.loading.isVisible = false
                        Toast.makeText(context, fetchResource.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.loading.isVisible = true
                    }
                    Resource.Status.FAILURE -> {
                        binding.loading.isVisible = false
                        Toast.makeText(context, fetchResource.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

}

private fun String.sanitize(): String {
    return if (this.contains("-")) {
        split("-").last()
    } else {
        this
    }
}
