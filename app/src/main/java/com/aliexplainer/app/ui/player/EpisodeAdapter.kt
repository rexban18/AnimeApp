package com.aliexplainer.app.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aliexplainer.app.R
import com.aliexplainer.app.data.model.Episode
import com.aliexplainer.app.databinding.ItemEpisodeBinding

class EpisodeAdapter(
    private val onItemClick: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var items: List<Episode> = emptyList()
    private var selectedEpisodeId: Int? = null

    fun submitList(list: List<Episode>) {
        items = list
        notifyDataSetChanged()
    }

    fun setSelectedEpisode(episodeId: Int) {
        selectedEpisodeId = episodeId
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode) {
            binding.episodeButton.text = "EP ${episode.episode_number ?: "?"}"

            val isSelected = episode.id == selectedEpisodeId
            binding.episodeButton.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (isSelected) R.color.black else R.color.episode_bg
                )
            )
            binding.episodeButton.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (isSelected) android.R.color.white else R.color.episode_text
                )
            )

            binding.root.setOnClickListener { onItemClick(episode) }
        }
    }
}
