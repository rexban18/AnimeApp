package com.aliexplainer.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliexplainer.app.data.model.Anime
import com.aliexplainer.app.databinding.ItemAnimeCardBinding
import com.bumptech.glide.Glide

class AnimeAdapter(
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    private var items: List<Anime> = emptyList()

    fun submitList(list: List<Anime>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class AnimeViewHolder(private val binding: ItemAnimeCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            Glide.with(binding.posterImage.context)
                .load(anime.poster_image)
                .centerCrop()
                .into(binding.posterImage)

            binding.titleText.text = anime.title
            binding.typeBadge.text = anime.type?.take(3)?.uppercase() ?: "ANI"

            binding.root.setOnClickListener { onItemClick(anime) }
        }
    }
}
