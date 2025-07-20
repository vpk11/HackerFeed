package com.vpk.hackerfeed.data.cache

/**
 * Configuration for cache settings.
 * Provides constants for cache expiration times and policies.
 */
object CacheConfig {
    
    // Cache expiration times in milliseconds
    const val TOP_STORIES_CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
    const val ARTICLE_CACHE_DURATION = 30 * 60 * 1000L // 30 minutes
    
    // Cache size limits
    const val MAX_CACHED_ARTICLES = 1000
    
    // Cache cleanup threshold (percentage of max size to trigger cleanup)
    const val CACHE_CLEANUP_THRESHOLD = 0.8f
    
    /**
     * Checks if cached data is still valid based on cache duration.
     * 
     * @param cachedAt The timestamp when data was cached
     * @param cacheDuration The cache duration in milliseconds
     * @return true if cache is still valid, false if expired
     */
    fun isCacheValid(cachedAt: Long, cacheDuration: Long): Boolean {
        return (System.currentTimeMillis() - cachedAt) < cacheDuration
    }
    
    /**
     * Checks if top stories cache is valid.
     */
    fun isTopStoriesCacheValid(cachedAt: Long): Boolean {
        return isCacheValid(cachedAt, TOP_STORIES_CACHE_DURATION)
    }
    
    /**
     * Checks if article cache is valid.
     */
    fun isArticleCacheValid(cachedAt: Long): Boolean {
        return isCacheValid(cachedAt, ARTICLE_CACHE_DURATION)
    }
}
