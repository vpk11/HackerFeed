package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.repository.FavouritesRepository

/**
 * Use case for removing an article from favourites.
 * Encapsulates the business logic for removing articles from favourites.
 */
class RemoveFromFavouritesUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    suspend operator fun invoke(articleId: Long): Result<Unit> {
        return try {
            favouritesRepository.removeFromFavourites(articleId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
