package com.vpk.hackerfeed.data.model

import com.google.gson.annotations.SerializedName

/**
 * API response model for HackerNews Article.
 * This represents the raw data structure returned by the HackerNews API.
 * It should only be used in the data layer for API parsing.
 */
data class ApiArticle(
    val id: Long,
    @SerializedName("by") val author: String?,
    val score: Int?,
    val time: Long?,
    val title: String?,
    val url: String?
)
