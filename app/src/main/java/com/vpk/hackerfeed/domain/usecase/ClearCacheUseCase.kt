package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.data.repository.NewsRepositoryImpl

/**
 * Use case for clearing all cached data.
 * This provides a clean way to manage cache clearing from the presentation layer.
 */
class ClearCacheUseCase(
    private val newsRepository: NewsRepositoryImpl
) {
    suspend operator fun invoke(): Result<Unit> {
        return newsRepository.clearCache()
    }
}
