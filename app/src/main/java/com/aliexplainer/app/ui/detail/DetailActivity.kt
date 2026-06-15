package com.aliexplainer.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aliexplainer.app.data.repository.AnimeRepository
import com.aliexplainer.app.databinding.ActivityDetailBinding
import com.aliexplainer.app.ui.player.PlayerActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val repository = AnimeRepository()
    private var animeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animeId = intent.getIntExtra("anime_id", 0)
        if (animeId == 0) {
            Toast.makeText(this, "Anime not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.backButton.setOnClickListener { finish() }
        binding.watchNowButton.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("anime_id", animeId)
            startActivity(intent)
        }

        loadAnimeDetail()
    }

    private fun loadAnimeDetail() {
        lifecycleScope.launch {
            try {
                val res = repository.getAnimeDetail(animeId)
                if (res.success && res.data != null) {
                    val anime = res.data

                    Glide.with(this@DetailActivity)
                        .load(anime.poster_image)
                        .centerCrop()
                        .into(binding.posterImage)

                    binding.titleText.text = anime.title

                    // Genre tags
                    anime.genre?.split(",")?.forEach { genre ->
                        val chip = layoutInflater.inflate(
                            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                            binding.genreContainer,
                            false
                        ) as android.widget.TextView
                        chip.text = genre.trim()
                        chip.setTextColor(android.graphics.Color.GRAY)
                        chip.textSize = 10f
                        chip.setTypeface(null, android.graphics.Typeface.BOLD)
                        binding.genreContainer.addView(chip)
                    }

                    binding.episodeCountText.text = "${anime.episode_count ?: 0} Episodes"
                    binding.ratingText.text = "Score: ${anime.rating ?: "N/A"}"
                    binding.descriptionText.text = anime.description ?: "No description available."
                    binding.typeText.text = anime.type?.replaceFirstChar { it.uppercase() } ?: "Anime"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DetailActivity, "Failed to load details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
