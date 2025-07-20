package com.vpk.hackerfeed.domain.repository

import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for favourites operations.
 * This defines the contract for favourite articles data operations in the domain layer.
 */
interface FavouritesRepository {
    fun getAllFavourites(): Flow<List<FavouriteArticle>>
    suspend fun isFavourite(articleId: Long): Boolean
    suspend fun addToFavourites(article: Article): Result<Unit>
    suspend fun removeFromFavourites(articleId: Long): Result<Unit>
    suspend fun toggleFavourite(article: Article): Result<Unit>
}
