package com.aliexplainer.app.data.model

data class AnimeResponse(
    val success: Boolean,
    val count: Int?,
    val data: List<Anime>?
)

data class Anime(
    val id: Int,
    val title: String,
    val poster_image: String?,
    val banner_image: String?,
    val type: String?,
    val episode_count: Int?,
    val rating: String?,
    val genre: String?,
    val description: String?,
    val slug: String?,
    val is_featured: Int?,
    val created_at: String?
)
