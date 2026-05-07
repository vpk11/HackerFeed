package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.model.StoryType
import com.vpk.hackerfeed.domain.repository.NewsRepository

/**
 * Use case for getting top story IDs.
 * Encapsulates the business logic for fetching top stories.
 */
class GetTopStoriesUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(
        storyType: StoryType = StoryType.TOP,
        forceRefresh: Boolean = false
    ): Result<List<Long>> {
        return newsRepository.getStoryIds(storyType, forceRefresh)
    }
}
