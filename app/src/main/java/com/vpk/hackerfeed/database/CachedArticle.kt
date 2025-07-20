package com.vpk.hackerfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for caching articles locally.
 * This stores articles retrieved from the API with timestamp for cache expiration.
 */
@Entity(tableName = "cached_articles")
data class CachedArticle(
    @PrimaryKey
    val id: Long,
    val author: String?,
    val score: Int?,
    val time: Long?,
    val title: String?,
    val url: String?,
    val cachedAt: Long = System.currentTimeMillis() // Timestamp when the article was cached
)
