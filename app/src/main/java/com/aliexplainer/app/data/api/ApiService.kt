package com.aliexplainer.app.data.api

import com.aliexplainer.app.data.model.AnimeDetailResponse
import com.aliexplainer.app.data.model.AnimeResponse
import com.aliexplainer.app.data.model.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api_banner.php")
    suspend fun getBanners(): AnimeResponse

    @GET("api_anime_list.php")
    suspend fun getAnimeList(@Query("type") type: String? = null): AnimeResponse

    @GET("api_anime_detail.php")
    suspend fun getAnimeDetail(@Query("id") id: Int): AnimeDetailResponse

    @GET("api_episodes.php")
    suspend fun getEpisodes(@Query("anime_id") animeId: Int): EpisodeResponse
}
