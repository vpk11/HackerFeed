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
            try {
                if (forceRefresh) {
                    // Force refresh: always fetch from remote and update cache
                    val remoteStories = remoteDataSource.getTopStoryIds()
                    localDataSource.cacheTopStories(remoteStories)
                    Result.success(remoteStories)
                } else {
                    // Normal flow: try cache first, then remote
                    val cachedStories = localDataSource.getCachedTopStories()
                    if (cachedStories != null) {
                        Result.success(cachedStories)
                    } else {
                        // Cache miss, fetch from remote
                        val remoteStories = remoteDataSource.getTopStoryIds()
                        // Cache the result for future use
                        localDataSource.cacheTopStories(remoteStories)
                        Result.success(remoteStories)
                    }
                }
            } catch (e: Exception) {
                // If we have cached data, return it even if it's expired (fallback strategy)
                val cachedStories = try {
                    localDataSource.getCachedTopStories()
                } catch (cacheException: Exception) {
                    null
                }
                
                if (cachedStories != null) {
                    Result.success(cachedStories)
                } else {
                    Result.failure(e)
                }
            }
        }
    }
    
    override suspend fun getArticleDetails(id: Long, forceRefresh: Boolean): Result<Article> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceRefresh) {
                    // Force refresh: always fetch from remote and update cache
                    val remoteArticle = remoteDataSource.getArticleDetails(id)
                    localDataSource.cacheArticle(remoteArticle)
                    Result.success(remoteArticle)
                } else {
                    // Normal flow: try cache first, then remote
                    val cachedArticle = localDataSource.getCachedArticle(id)
                    if (cachedArticle != null) {
                        Result.success(cachedArticle)
                    } else {
                        // Cache miss, fetch from remote
                        val remoteArticle = remoteDataSource.getArticleDetails(id)
                        // Cache the result for future use
                        localDataSource.cacheArticle(remoteArticle)
                        Result.success(remoteArticle)
                    }
                }
            } catch (e: Exception) {
                // If we have cached data, return it even if it's expired (fallback strategy)
                val cachedArticle = try {
                    localDataSource.getCachedArticle(id)
                } catch (cacheException: Exception) {
                    null
                }
                
                if (cachedArticle != null) {
                    Result.success(cachedArticle)
                } else {
                    Result.failure(e)
                }
            }
        }
    }
    
    /**
     * Batch fetch multiple articles with caching support.
     * This method optimizes cache usage by fetching only missing articles from remote.
     */
    suspend fun getMultipleArticleDetails(ids: List<Long>): Result<Map<Long, Article>> {
        return withContext(Dispatchers.IO) {
            try {
                // Get cached articles
                val cachedArticles = localDataSource.getCachedArticles(ids)
                val missingIds = ids - cachedArticles.keys
                
                if (missingIds.isEmpty()) {
                    // All articles are cached
                    Result.success(cachedArticles)
                } else {
                    // Fetch missing articles from remote
                    val remoteArticles = mutableMapOf<Long, Article>()
                    val errors = mutableListOf<Exception>()
                    
                    for (id in missingIds) {
                        try {
                            val article = remoteDataSource.getArticleDetails(id)
                            remoteArticles[id] = article
                        } catch (e: Exception) {
                            errors.add(e)
                        }
                    }
                    
                    // Cache the newly fetched articles
                    if (remoteArticles.isNotEmpty()) {
                        localDataSource.cacheArticles(remoteArticles.values.toList())
                    }
                    
                    // Combine cached and remote articles
                    val allArticles = cachedArticles + remoteArticles
                    
                    if (errors.isNotEmpty() && allArticles.isEmpty()) {
                        Result.failure(errors.first())
                    } else {
                        Result.success(allArticles)
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
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
