package com.example.igeniusandroidtest.ui.detailed

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.igeniusandroidtest.R
import com.example.igeniusandroidtest.data.source.local.Repository
import com.example.igeniusandroidtest.databinding.FragmentDetailedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailedFragment : Fragment() {
    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailedViewModel>()
    private val args by navArgs<DetailedFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setQuery(args.id)
        subscribeRepository()
        subscribeIsStarred()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeRepository() {
        viewModel.repository.observe(viewLifecycleOwner) {
            it?.let { repository ->
                lifecycleScope.launch {
                    viewModel.checkStarredRepository(repository.owner?.login ?: "", repository.name ?: "")
                }
                bindData(repository)
                performStarButton(repository.owner?.login ?: "", repository.name ?: "")
            }
        }
    }

    private fun subscribeIsStarred() {
        viewModel.isStarred.observe(viewLifecycleOwner) { isStarred ->
            binding.buttonStar.apply {
                iconTint = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(), if (isStarred) R.color.purple_200 else R.color.white
                    )
                )
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (isStarred) R.color.purple_200 else R.color.white
                    )
                )
            }
        }
    }

    private fun performStarButton(userName: String, repositoryName: String) {
        binding.buttonStar.setOnClickListener {
            viewModel.starRepository(userName, repositoryName)
        }
    }

    private fun bindData(repository: Repository) = with(binding) {
        textViewId.text = repository.id?.toString() ?: ""
        textViewName.text = repository.owner?.login ?: ""
        textViewDescription.text = repository.description ?: ""
        textViewLanguage.text = repository.language?.toString() ?: ""
    }
}