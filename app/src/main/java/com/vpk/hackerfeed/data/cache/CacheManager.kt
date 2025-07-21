package com.vpk.hackerfeed.data.cache

import com.vpk.hackerfeed.database.CachedArticle
import com.vpk.hackerfeed.database.CachedArticleDao
import com.vpk.hackerfeed.database.CachedTopStoriesDao
import com.vpk.hackerfeed.database.CachedTopStories
import com.vpk.hackerfeed.domain.model.Article
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Cache manager for handling article and top stories caching.
 * Provides methods for cache operations and cleanup.
 */
class CacheManager(
    private val cachedArticleDao: CachedArticleDao,
    private val cachedTopStoriesDao: CachedTopStoriesDao
) {
    private val mutex = Mutex()
    
    // In-memory cache for frequently accessed data
    private var memoryTopStories: Pair<List<Long>, Long>? = null
    
    /**
     * Gets cached top stories if they are still valid.
     * First checks memory cache, then database cache.
     */
    suspend fun getCachedTopStories(): List<Long>? = mutex.withLock {
        // Check memory cache first
        memoryTopStories?.let { (stories, cachedAt) ->
            if (CacheConfig.isTopStoriesCacheValid(cachedAt)) {
                return@withLock stories
            } else {
                memoryTopStories = null
            }
        }
        
        // Check database cache
        val cachedTopStories = cachedTopStoriesDao.getCachedTopStories()
        return@withLock if (cachedTopStories != null && 
                          CacheConfig.isTopStoriesCacheValid(cachedTopStories.cachedAt)) {
            val stories = cachedTopStories.storyIds
            // Update memory cache
            memoryTopStories = Pair(stories, cachedTopStories.cachedAt)
            stories
        } else {
            null
        }
    }
    
    /**
     * Caches top stories in both memory and database.
     */
    suspend fun cacheTopStories(storyIds: List<Long>) = mutex.withLock {
        val currentTime = System.currentTimeMillis()
        
        // Cache in database
        cachedTopStoriesDao.insertCachedTopStories(
            CachedTopStories(
                storyIds = storyIds,
                cachedAt = currentTime
            )
        )
        
        // Cache in memory
        memoryTopStories = Pair(storyIds, currentTime)
    }
    
    /**
     * Gets a cached article if it's still valid.
     */
    suspend fun getCachedArticle(id: Long): Article? {
        val cachedArticle = cachedArticleDao.getCachedArticle(id)
        return if (cachedArticle != null && 
                  CacheConfig.isArticleCacheValid(cachedArticle.cachedAt)) {
            cachedArticle.toDomainModel()
        } else {
            null
        }
    }
    
    /**
     * Gets multiple cached articles that are still valid.
     */
    suspend fun getCachedArticles(ids: List<Long>): Map<Long, Article> {
        val cachedArticles = cachedArticleDao.getCachedArticles(ids)
        
        return cachedArticles
            .filter { CacheConfig.isArticleCacheValid(it.cachedAt) }
            .associate { cachedArticle ->
                cachedArticle.id to cachedArticle.toDomainModel()
            }
    }
    
    /**
     * Caches a single article.
     */
    suspend fun cacheArticle(article: Article) {
        val cachedArticle = CachedArticle(
            id = article.id,
            author = article.author,
            score = article.score,
            time = article.time,
            title = article.title,
            url = article.url,
            cachedAt = System.currentTimeMillis()
        )
        cachedArticleDao.insertCachedArticle(cachedArticle)
        
        // Check if cache cleanup is needed
        performCacheCleanupIfNeeded()
    }
    
    /**
     * Caches multiple articles.
     */
    suspend fun cacheArticles(articles: List<Article>) {
        val currentTime = System.currentTimeMillis()
        val cachedArticles = articles.map { article ->
            CachedArticle(
                id = article.id,
                author = article.author,
                score = article.score,
                time = article.time,
                title = article.title,
                url = article.url,
                cachedAt = currentTime
            )
        }
        cachedArticleDao.insertCachedArticles(cachedArticles)
        
        // Check if cache cleanup is needed
        performCacheCleanupIfNeeded()
    }
    
    /**
     * Clears all cached data.
     */
    suspend fun clearAllCache() = mutex.withLock {
        cachedArticleDao.clearAllCachedArticles()
        cachedTopStoriesDao.clearCachedTopStories()
        memoryTopStories = null
    }
    
    /**
     * Clears expired cache entries.
     */
    suspend fun clearExpiredCache() = mutex.withLock {
        val currentTime = System.currentTimeMillis()
        
        // Clear expired articles
        val expiredArticleTime = currentTime - CacheConfig.ARTICLE_CACHE_DURATION
        cachedArticleDao.deleteExpiredArticles(expiredArticleTime)
        
        // Clear expired top stories from memory
        memoryTopStories?.let { (_, cachedAt) ->
            if (!CacheConfig.isTopStoriesCacheValid(cachedAt)) {
                memoryTopStories = null
            }
        }
        
        // Clear expired top stories from database
        val cachedTopStories = cachedTopStoriesDao.getCachedTopStories()
        if (cachedTopStories != null && 
            !CacheConfig.isTopStoriesCacheValid(cachedTopStories.cachedAt)) {
            cachedTopStoriesDao.clearCachedTopStories()
        }
    }
    
    /**
     * Performs cache cleanup if the cache size exceeds the maximum limit.
     */
    private suspend fun performCacheCleanupIfNeeded() {
        val cacheCount = cachedArticleDao.getCachedArticleCount()
        if (cacheCount > CacheConfig.MAX_CACHED_ARTICLES) {
            // Keep only the most recent articles up to the limit
            val recentArticles = cachedArticleDao.getRecentCachedArticles(CacheConfig.MAX_CACHED_ARTICLES)
            // Use atomic operation to prevent data loss if operation fails
            cachedArticleDao.replaceAllCachedArticles(recentArticles)
        }
    }
}

// Extension function for mapping between database entity and domain model
private fun CachedArticle.toDomainModel(): Article = Article(
    id = this.id,
    author = this.author,
    score = this.score,
    time = this.time,
    title = this.title,
    url = this.url
)
