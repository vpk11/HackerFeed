package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.domain.model.Article

/**
 * Data source interface for local news caching operations.
 * This abstracts the local cache implementation for news data.
 */
interface LocalNewsDataSource {
    suspend fun getCachedTopStories(): List<Long>?
    suspend fun cacheTopStories(storyIds: List<Long>)
    suspend fun getCachedArticle(id: Long): Article?
    suspend fun getCachedArticles(ids: List<Long>): Map<Long, Article>
    suspend fun cacheArticle(article: Article)
    suspend fun cacheArticles(articles: List<Article>)
    suspend fun clearCache()
    suspend fun clearExpiredCache()
}
