package com.vpk.hackerfeed.domain.repository

import com.vpk.hackerfeed.domain.model.Article

/**
 * Repository interface for news operations.
 * This defines the contract for news data operations in the domain layer.
 */
interface NewsRepository {
    suspend fun getTopStoryIds(): Result<List<Long>>
    suspend fun getArticleDetails(id: Long): Result<Article>
}
