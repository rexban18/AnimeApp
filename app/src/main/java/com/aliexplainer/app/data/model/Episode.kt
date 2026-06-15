package com.aliexplainer.app.data.model

data class EpisodeResponse(
    val success: Boolean,
    val anime: EpisodeAnimeInfo?,
    val count: Int?,
    val data: List<Episode>?,
    val message: String?
)

data class EpisodeAnimeInfo(
    val id: Int,
    val title: String,
    val type: String?
)

data class Episode(
    val id: Int,
    val anime_id: Int?,
    val episode_number: Int?,
    val title: String?,
    val server1_url: String?,
    val server2_url: String?,
    val duration: String?,
    val created_at: String?
)

data class AnimeDetailResponse(
    val success: Boolean,
    val data: Anime?,
    val message: String?
)
