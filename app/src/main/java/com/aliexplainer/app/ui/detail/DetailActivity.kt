package com.aliexplainer.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aliexplainer.app.data.repository.AnimeRepository
import com.aliexplainer.app.databinding.ActivityDetailBinding
import com.aliexplainer.app.ui.player.PlayerActivity
import com.aliexplainer.app.utils.DebugConsole
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
        DebugConsole.apiRequest("api_anime_detail.php?id=$animeId")
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
                    binding.genreContainer.removeAllViews()
                    anime.genre?.split(",")?.forEach { genre ->
                        val tag = android.widget.TextView(this@DetailActivity).apply {
                            text = genre.trim()
                            setTextColor(android.graphics.Color.GRAY)
                            textSize = 10f
                            setTypeface(null, android.graphics.Typeface.BOLD)
                        }
                        binding.genreContainer.addView(tag)
                        val dot = android.widget.TextView(this@DetailActivity).apply {
                            text = " \u2022 "
                            setTextColor(android.graphics.Color.parseColor("#CCCCCC"))
                            textSize = 10f
                        }
                        binding.genreContainer.addView(dot)
                    }

                    binding.episodeCountText.text = "${anime.episode_count ?: 0} Episodes"
                    binding.ratingText.text = "Score: ${anime.rating ?: "N/A"}"
                    binding.descriptionText.text = anime.description ?: "No description available."
                    binding.typeText.text = anime.type?.capitalize() ?: "Anime"
                    DebugConsole.apiSuccess("api_anime_detail.php?id=$animeId")
                }
            } catch (e: Exception) {
                DebugConsole.apiError("api_anime_detail.php?id=$animeId", e.message ?: "Unknown")
                e.printStackTrace()
                Toast.makeText(this@DetailActivity, "Failed to load details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
