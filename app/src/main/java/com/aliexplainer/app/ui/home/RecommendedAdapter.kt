package com.aliexplainer.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliexplainer.app.data.model.Anime
import com.aliexplainer.app.databinding.ItemRecommendedBinding
import com.bumptech.glide.Glide

class RecommendedAdapter(
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    private var items: List<Anime> = emptyList()

    fun submitList(list: List<Anime>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class RecommendedViewHolder(private val binding: ItemRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            Glide.with(binding.posterImage.context)
                .load(anime.poster_image)
                .centerCrop()
                .into(binding.posterImage)

            binding.titleText.text = anime.title
            binding.typeBadge.text = anime.type?.uppercase() ?: "ANIME"

            binding.root.setOnClickListener { onItemClick(anime) }
        }
    }
}
