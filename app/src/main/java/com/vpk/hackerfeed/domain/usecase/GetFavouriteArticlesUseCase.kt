package com.vpk.hackerfeed.domain.usecase

import com.vpk.hackerfeed.domain.model.FavouriteArticle
import com.vpk.hackerfeed.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting all favourite articles.
 * Encapsulates the business logic for fetching favourite articles.
 */
class GetFavouriteArticlesUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    operator fun invoke(): Flow<List<FavouriteArticle>> {
        return favouritesRepository.getAllFavourites()
    }
}
