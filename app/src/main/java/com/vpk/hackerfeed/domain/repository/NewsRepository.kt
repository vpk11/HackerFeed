package com.vpk.hackerfeed.domain.repository

import com.vpk.hackerfeed.domain.model.Article

/**
 * Repository interface for news operations.
 * This defines the contract for news data operations in the domain layer.
 */
interface NewsRepository {
    suspend fun getTopStoryIds(forceRefresh: Boolean = false): Result<List<Long>>
    suspend fun getArticleDetails(id: Long, forceRefresh: Boolean = false): Result<Article>
    suspend fun clearCache(): Result<Unit>
    suspend fun clearExpiredCache(): Result<Unit>
}
