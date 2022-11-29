package com.example.igeniusandroidtest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.igeniusandroidtest.databinding.RepositoryItemAdapterBinding
import com.example.igeniusandroidtest.model.AuthUserReposItem

class RepositoryAdapter(
    private val repositories: List<AuthUserReposItem>,
    private val onCardRepositoryClick: (AuthUserReposItem) -> Unit
) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    inner class RepositoryViewHolder(
        private val binding: RepositoryItemAdapterBinding,
        private val onCardRepositoryClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(repository: AuthUserReposItem) = with(binding) {
            //imageCardRepositoryBg
            textViewUsername.text = repository.name
            textViewDescription.text = repository.description
            textViewLanguage.text = repository.language.toString()
            itemView.setOnClickListener { onCardRepositoryClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder =
        RepositoryViewHolder(
            RepositoryItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { itemPosition ->
            onCardRepositoryClick(repositories[itemPosition])
        }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) = holder.setData(repositories[position])

    override fun getItemCount(): Int = repositories.size
}