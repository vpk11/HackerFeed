package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.domain.model.FavouriteArticle
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for local favourites operations.
 * This abstracts the local data source implementation.
 */
interface LocalFavouritesDataSource {
    fun getAllFavourites(): Flow<List<FavouriteArticle>>
    suspend fun getFavouriteById(id: Long): FavouriteArticle?
    suspend fun isFavourite(id: Long): Boolean
    suspend fun insertFavourite(article: FavouriteArticle)
    suspend fun deleteFavouriteById(id: Long)
    suspend fun toggleFavourite(article: FavouriteArticle)
}
