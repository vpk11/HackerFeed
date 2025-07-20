package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.data.cache.CacheManager
import com.vpk.hackerfeed.domain.model.Article

/**
 * Implementation of LocalNewsDataSource using Room database cache.
 * This handles all local caching operations for news data.
 */
class RoomNewsDataSource(
    private val cacheManager: CacheManager
) : LocalNewsDataSource {
    
    override suspend fun getCachedTopStories(): List<Long>? {
        return cacheManager.getCachedTopStories()
    }
    
    override suspend fun cacheTopStories(storyIds: List<Long>) {
        cacheManager.cacheTopStories(storyIds)
    }
    
    override suspend fun getCachedArticle(id: Long): Article? {
        return cacheManager.getCachedArticle(id)
    }
    
    override suspend fun getCachedArticles(ids: List<Long>): Map<Long, Article> {
        return cacheManager.getCachedArticles(ids)
    }
    
    override suspend fun cacheArticle(article: Article) {
        cacheManager.cacheArticle(article)
    }
    
    override suspend fun cacheArticles(articles: List<Article>) {
        cacheManager.cacheArticles(articles)
    }
    
    override suspend fun clearCache() {
        cacheManager.clearAllCache()
    }
    
    override suspend fun clearExpiredCache() {
        cacheManager.clearExpiredCache()
    }
}
