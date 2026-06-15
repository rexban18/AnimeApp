package com.aliexplainer.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliexplainer.app.data.model.Anime
import com.aliexplainer.app.data.repository.AnimeRepository
import com.aliexplainer.app.databinding.ActivityHomeBinding
import com.aliexplainer.app.ui.detail.DetailActivity
import com.aliexplainer.app.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val repository = AnimeRepository()

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var animeAdapter: AnimeAdapter

    private val banners = mutableListOf<Anime>()
    private val animeList = mutableListOf<Anime>()

    private var bannerAutoSlider: Handler? = null
    private var bannerSliderRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

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

        binding.bannerViewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })

        bannerAutoSlider = Handler(Looper.getMainLooper())
        bannerSliderRunnable = Runnable {
            if (banners.isNotEmpty()) {
                val nextItem = (binding.bannerViewPager.currentItem + 1) % banners.size
                binding.bannerViewPager.setCurrentItem(nextItem, true)
                bannerAutoSlider?.postDelayed(bannerSliderRunnable!!, 4000)
            }
        }
    }

    private fun setupDots(count: Int) {
        binding.bannerDots.removeAllViews()
        for (i in 0 until count) {
            val dot = TextView(this)
            dot.layoutParams = LinearLayout.LayoutParams(36, 36).apply {
                setMargins(4, 0, 4, 0)
            }
            dot.textSize = 14f
            dot.text = "\u2022"
            dot.gravity = android.view.Gravity.CENTER
            dot.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            binding.bannerDots.addView(dot)
        }
        if (count > 0) updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in 0 until binding.bannerDots.childCount) {
            val dot = binding.bannerDots.getChildAt(i) as TextView
            dot.setTextColor(
                ContextCompat.getColor(this,
                    if (i == position) android.R.color.white else android.R.color.darker_gray
                )
            )
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
        binding.filterAll.isChecked = true
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
                val bannerRes = repository.getBanners()
                if (bannerRes.success && bannerRes.data != null) {
                    banners.clear()
                    banners.addAll(bannerRes.data)
                    bannerAdapter.submitList(banners.toList())
                    setupDots(banners.size)
                    bannerAutoSlider?.postDelayed(bannerSliderRunnable!!, 4000)
                }
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

                    val recommended = list.filter { it.type == "donghua" }.take(10)
                    recommendedAdapter.submitList(recommended)

                    animeList.clear()
                    animeList.addAll(list)
                    animeAdapter.submitList(animeList)

                    binding.noResultsText.visibility =
                        if (animeList.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
