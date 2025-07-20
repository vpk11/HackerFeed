package com.vpk.hackerfeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for managing cached top stories.
 * Provides methods for caching and retrieving top story IDs.
 */
@Dao
interface CachedTopStoriesDao {
    
    @Query("SELECT * FROM cached_top_stories WHERE id = 1")
    suspend fun getCachedTopStories(): CachedTopStories?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTopStories(topStories: CachedTopStories)
    
    @Query("DELETE FROM cached_top_stories")
    suspend fun clearCachedTopStories()
}
