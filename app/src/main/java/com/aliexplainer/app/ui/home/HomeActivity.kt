package com.aliexplainer.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.aliexplainer.app.data.model.Anime
import com.aliexplainer.app.data.repository.AnimeRepository
import com.aliexplainer.app.databinding.ActivityHomeBinding
import com.aliexplainer.app.ui.detail.DetailActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val repository = AnimeRepository()

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var animeAdapter: AnimeAdapter

    private val banners = mutableListOf<Anime>()
    private val recommendedList = mutableListOf<Anime>()
    private val animeList = mutableListOf<Anime>()

    private var bannerAutoSlider: Handler? = null
    private var bannerSliderRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBannerSlider()
        setupRecommendedList()
        setupAnimeGrid()
        setupCategoryFilter()
        loadData()
    }

    private fun setupBannerSlider() {
        bannerAdapter = BannerAdapter { anime ->
            navigateToDetail(anime.id)
        }
        binding.bannerViewPager.adapter = bannerAdapter
        binding.bannerViewPager.offscreenPageLimit = 1

        // Auto slide banner
        bannerAutoSlider = Handler(Looper.getMainLooper())
        bannerSliderRunnable = Runnable {
            val currentItem = binding.bannerViewPager.currentItem
            val nextItem = if (currentItem < banners.size - 1) currentItem + 1 else 0
            binding.bannerViewPager.setCurrentItem(nextItem, true)
            bannerAutoSlider?.postDelayed(bannerSliderRunnable!!, 4000)
        }
    }

    private fun setupRecommendedList() {
        recommendedAdapter = RecommendedAdapter { anime ->
            navigateToDetail(anime.id)
        }
        binding.recommendedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }
    }

    private fun setupAnimeGrid() {
        animeAdapter = AnimeAdapter { anime ->
            navigateToDetail(anime.id)
        }
        binding.animeGridRecyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = animeAdapter
        }
    }

    private fun setupCategoryFilter() {
        binding.filterAll.setOnClickListener { updateFilterSelection(binding.filterAll, null) }
        binding.filterDonghua.setOnClickListener { updateFilterSelection(binding.filterDonghua, "donghua") }
        binding.filterMovie.setOnClickListener { updateFilterSelection(binding.filterMovie, "movie") }
    }

    private fun updateFilterSelection(selectedTab: com.google.android.material.button.MaterialButton, type: String?) {
        binding.filterAll.isChecked = false
        binding.filterDonghua.isChecked = false
        binding.filterMovie.isChecked = false
        selectedTab.isChecked = true
        loadAnimeList(type)
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                // Load banners
                val bannerRes = repository.getBanners()
                if (bannerRes.success && bannerRes.data != null) {
                    banners.clear()
                    banners.addAll(bannerRes.data)
                    bannerAdapter.submitList(banners.toList())
                    binding.bannerDots.attachTo(binding.bannerViewPager)

                    // Start auto slider
                    bannerAutoSlider?.postDelayed(bannerSliderRunnable!!, 4000)
                }

                // Load all for recommended & grid
                loadAnimeList(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadAnimeList(type: String?) {
        lifecycleScope.launch {
            try {
                val res = repository.getAnimeList(type)
                if (res.success && res.data != null) {
                    val list = res.data

                    // Recommended = donghua type (first 10)
                    val recommended = list.filter { it.type == "donghua" }.take(10)
                    recommendedList.clear()
                    recommendedList.addAll(recommended)
                    recommendedAdapter.submitList(recommendedList.toList())

                    // All series
                    animeList.clear()
                    animeList.addAll(list)
                    animeAdapter.submitList(animeList.toList())

                    updateVisibility()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateVisibility() {
        binding.noResultsText.visibility =
            if (animeList.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun navigateToDetail(animeId: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("anime_id", animeId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAutoSlider?.removeCallbacks(bannerSliderRunnable!!)
    }
}
