package com.vpk.hackerfeed.domain.model

/**
 * Domain model for a Favourite Article.
 * This represents the business model for favourite articles.
 */
data class FavouriteArticle(
    val id: Long,
    val title: String?,
    val author: String?,
    val score: Int?,
    val time: Long?,
    val url: String?,
    val dateAdded: Long
)
