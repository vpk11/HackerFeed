package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.repository.NewsRepository

/**
 * Use case for getting top story IDs.
 * Encapsulates the business logic for fetching top stories.
 */
class GetTopStoriesUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<List<Long>> {
        return try {
            newsRepository.getTopStoryIds(forceRefresh)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
