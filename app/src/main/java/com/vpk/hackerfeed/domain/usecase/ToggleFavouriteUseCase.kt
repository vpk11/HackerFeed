package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.repository.FavouritesRepository

/**
 * Use case for toggling favourite status of an article.
 * Encapsulates the business logic for adding/removing articles from favourites.
 */
class ToggleFavouriteUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    suspend operator fun invoke(article: Article): Result<Unit> {
        return try {
            favouritesRepository.toggleFavourite(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
