package com.vpk.hackerfeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * DAO for managing cached articles.
 * Provides methods for caching, retrieving, and managing article cache.
 */
@Dao
interface CachedArticleDao {
    
    @Query("SELECT * FROM cached_articles WHERE id = :id")
    suspend fun getCachedArticle(id: Long): CachedArticle?
    
    @Query("SELECT * FROM cached_articles WHERE id IN (:ids)")
    suspend fun getCachedArticles(ids: List<Long>): List<CachedArticle>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedArticle(article: CachedArticle)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedArticles(articles: List<CachedArticle>)
    
    @Query("DELETE FROM cached_articles WHERE cachedAt < :expiredTimestamp")
    suspend fun deleteExpiredArticles(expiredTimestamp: Long)
    
    @Query("DELETE FROM cached_articles")
    suspend fun clearAllCachedArticles()
    
    @Query("SELECT COUNT(*) FROM cached_articles")
    suspend fun getCachedArticleCount(): Int
    
    @Query("SELECT * FROM cached_articles ORDER BY cachedAt DESC LIMIT :limit")
    suspend fun getRecentCachedArticles(limit: Int): List<CachedArticle>
    
    /**
     * Atomically replaces all cached articles with the provided list.
     * This ensures that if the operation fails, the cache remains in a consistent state.
     */
    @Transaction
    suspend fun replaceAllCachedArticles(articles: List<CachedArticle>) {
        clearAllCachedArticles()
        insertCachedArticles(articles)
    }
}
