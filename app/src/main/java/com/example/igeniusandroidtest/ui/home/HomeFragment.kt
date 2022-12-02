package com.example.igeniusandroidtest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.igeniusandroidtest.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consumeUiState()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun consumeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.produceHomeUiState.collect { homeUiState ->
                bindData(homeUiState)
            }
        }
    }

    private fun bindData(homeUiState: HomeUiState) = with(binding) {
        progressLoading.isVisible = homeUiState.isLoading
        recyclerViewRepos.adapter = RepositoryAdapter(homeUiState.cards) { repo ->
            findNavController().navigate(HomeFragmentDirections.goToDetail(repo.id ?: -1))
        }
    }
}