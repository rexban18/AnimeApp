package com.aliexplainer.app.ui.player

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.GridLayoutManager
import com.aliexplainer.app.data.model.Episode
import com.aliexplainer.app.data.repository.AnimeRepository
import com.aliexplainer.app.databinding.ActivityPlayerBinding
import kotlinx.coroutines.launch

@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val repository = AnimeRepository()

    private var player: ExoPlayer? = null
    private var animeId: Int = 0
    private var episodes: List<Episode> = emptyList()
    private var currentEpisode: Episode? = null

    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animeId = intent.getIntExtra("anime_id", 0)
        if (animeId == 0) {
            Toast.makeText(this, "Invalid anime", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.backButton.setOnClickListener { finish() }

        setupEpisodeList()
        loadEpisodes()
    }

    private fun setupEpisodeList() {
        episodeAdapter = EpisodeAdapter { episode ->
            playEpisode(episode)
        }
        binding.episodeRecyclerView.apply {
            layoutManager = GridLayoutManager(this@PlayerActivity, 4)
            adapter = episodeAdapter
        }
    }

    private fun loadEpisodes() {
        lifecycleScope.launch {
            try {
                val res = repository.getEpisodes(animeId)
                if (res.success && res.data != null) {
                    episodes = res.data
                    episodeAdapter.submitList(episodes)
                    if (episodes.isNotEmpty()) {
                        playEpisode(episodes.first())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PlayerActivity, "Failed to load episodes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playEpisode(episode: Episode) {
        currentEpisode = episode
        val videoUrl = episode.server1_url ?: episode.server2_url

        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "No video URL", Toast.LENGTH_SHORT).show()
            return
        }

        binding.episodeTitleText.text = "Episode ${episode.episode_number ?: ""}"
        player?.release()

        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(videoUrl)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()

        episodeAdapter.setSelectedEpisode(episode.id)
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
