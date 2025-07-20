package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.data.repository.NewsRepositoryImpl

/**
 * Use case for clearing expired cache entries.
 * This provides automatic cache cleanup functionality.
 */
class ClearExpiredCacheUseCase(
    private val newsRepository: NewsRepositoryImpl
) {
    suspend operator fun invoke(): Result<Unit> {
        return newsRepository.clearExpiredCache()
    }
}
