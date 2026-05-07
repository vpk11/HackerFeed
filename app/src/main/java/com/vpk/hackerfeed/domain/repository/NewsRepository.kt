package com.vpk.hackerfeed.domain.repository

import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.StoryType

/**
 * Repository interface for news operations.
 * This defines the contract for news data operations in the domain layer.
 */
interface NewsRepository {
    suspend fun getStoryIds(storyType: StoryType = StoryType.TOP, forceRefresh: Boolean = false): Result<List<Long>>
    suspend fun getArticleDetails(id: Long, forceRefresh: Boolean = false): Result<Article>
    suspend fun clearCache(): Result<Unit>
    suspend fun clearExpiredCache(): Result<Unit>
}
