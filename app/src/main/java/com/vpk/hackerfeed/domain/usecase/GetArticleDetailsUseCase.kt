package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.repository.NewsRepository

/**
 * Use case for getting article details by ID.
 * Encapsulates the business logic for fetching article details.
 */
class GetArticleDetailsUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(articleId: Long, forceRefresh: Boolean = false): Result<Article> {
        return newsRepository.getArticleDetails(articleId, forceRefresh)
    }
}
