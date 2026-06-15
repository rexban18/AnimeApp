package com.aliexplainer.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliexplainer.app.data.model.Anime
import com.aliexplainer.app.databinding.ItemBannerBinding
import com.bumptech.glide.Glide

class BannerAdapter(
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private var items: List<Anime> = emptyList()

    fun submitList(list: List<Anime>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            Glide.with(binding.bannerImage.context)
                .load(anime.banner_image)
                .centerCrop()
                .into(binding.bannerImage)

            binding.bannerTitle.text = anime.title
            binding.bannerType.text = anime.type?.uppercase() ?: "ANIME"
            binding.bannerDescription.text = anime.title

            binding.root.setOnClickListener { onItemClick(anime) }
        }
    }
}
