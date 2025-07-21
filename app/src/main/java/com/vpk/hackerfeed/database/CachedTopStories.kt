package com.vpk.hackerfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for caching top story IDs.
 * This stores the list of top story IDs with cache timestamp.
 */
@Entity(tableName = "cached_top_stories")
data class CachedTopStories(
    @PrimaryKey
    val id: Int = 1, // Single row table
    val storyIds: List<Long>, // List of story IDs
    val cachedAt: Long = System.currentTimeMillis()
)
