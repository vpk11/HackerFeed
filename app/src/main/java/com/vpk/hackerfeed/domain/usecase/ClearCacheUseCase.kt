package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.repository.NewsRepository

/**
 * Use case for clearing all cached data.
 * This provides a clean way to manage cache clearing from the presentation layer.
 */
class ClearCacheUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return newsRepository.clearCache()
    }
}
