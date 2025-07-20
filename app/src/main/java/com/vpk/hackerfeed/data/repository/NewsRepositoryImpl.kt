package com.vpk.hackerfeed.data.repository

import com.vpk.hackerfeed.data.datasource.LocalNewsDataSource
import com.vpk.hackerfeed.data.datasource.RemoteNewsDataSource
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of NewsRepository with caching support.
 * This class coordinates between local cache and remote data sources.
 * 
 * Cache Strategy:
 * 1. Check local cache first
 * 2. If cache miss or expired, fetch from remote
 * 3. Cache the remote response for future use
 * 4. Return the data
 */
class NewsRepositoryImpl(
    private val remoteDataSource: RemoteNewsDataSource,
    private val localDataSource: LocalNewsDataSource
) : NewsRepository {
    
    override suspend fun getTopStoryIds(forceRefresh: Boolean): Result<List<Long>> {
        return withContext(Dispatchers.IO) {
            val cachedStories = if (!forceRefresh) localDataSource.getCachedTopStories() else null
            if (cachedStories != null) {
                return@withContext Result.success(cachedStories)
            }

            try {
                val remoteStories = remoteDataSource.getTopStoryIds()
                localDataSource.cacheTopStories(remoteStories)
                Result.success(remoteStories)
            } catch (e: Exception) {
                // Fallback to any available cache, even if expired
                val fallbackCache = try { localDataSource.getCachedTopStories() } catch (cacheEx: Exception) { null }
                fallbackCache?.let { Result.success(it) } ?: Result.failure(e)
            }
        }
    }
    
    override suspend fun getArticleDetails(id: Long, forceRefresh: Boolean): Result<Article> {
        return withContext(Dispatchers.IO) {
            val cachedArticle = if (!forceRefresh) localDataSource.getCachedArticle(id) else null
            if (cachedArticle != null) {
                return@withContext Result.success(cachedArticle)
            }

            try {
                val remoteArticle = remoteDataSource.getArticleDetails(id)
                localDataSource.cacheArticle(remoteArticle)
                Result.success(remoteArticle)
            } catch (e: Exception) {
                // Fallback to any available cache, even if expired
                val fallbackCache = try { localDataSource.getCachedArticle(id) } catch (cacheEx: Exception) { null }
                fallbackCache?.let { Result.success(it) } ?: Result.failure(e)
            }
        }
    }

    /**
     * Clears all cached data.
     */
    override suspend fun clearCache(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.clearCache()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Clears expired cache entries.
     */
    override suspend fun clearExpiredCache(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.clearExpiredCache()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
