package com.aliexplainer.app.data.repository

import com.aliexplainer.app.data.api.RetrofitClient
import com.aliexplainer.app.data.model.*

class AnimeRepository {

    private val api = RetrofitClient.apiService

    suspend fun getBanners(): AnimeResponse {
        return api.getBanners()
    }

    suspend fun getAnimeList(type: String? = null): AnimeResponse {
        return api.getAnimeList(type)
    }

    suspend fun getAnimeDetail(id: Int): AnimeDetailResponse {
        return api.getAnimeDetail(id)
    }

    suspend fun getEpisodes(animeId: Int): EpisodeResponse {
        return api.getEpisodes(animeId)
    }
}
