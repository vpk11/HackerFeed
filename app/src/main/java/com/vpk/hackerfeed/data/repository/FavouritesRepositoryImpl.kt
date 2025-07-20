package com.vpk.hackerfeed.data.repository

import com.vpk.hackerfeed.data.datasource.LocalFavouritesDataSource
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import com.vpk.hackerfeed.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of FavouritesRepository.
 * This class coordinates between different data sources for favourites operations.
 */
class FavouritesRepositoryImpl(
    private val localDataSource: LocalFavouritesDataSource
) : FavouritesRepository {
    
    override fun getAllFavourites(): Flow<List<FavouriteArticle>> {
        return localDataSource.getAllFavourites()
    }
    
    override suspend fun isFavourite(articleId: Long): Boolean {
        return localDataSource.isFavourite(articleId)
    }
    
    override suspend fun addToFavourites(article: Article): Result<Unit> {
        return try {
            val favouriteArticle = FavouriteArticle(
                id = article.id,
                title = article.title,
                author = article.author,
                score = article.score,
                time = article.time,
                url = article.url,
                dateAdded = System.currentTimeMillis()
            )
            localDataSource.insertFavourite(favouriteArticle)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFromFavourites(articleId: Long): Result<Unit> {
        return try {
            localDataSource.deleteFavouriteById(articleId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun toggleFavourite(article: Article): Result<Unit> {
        return try {
            val favouriteArticle = FavouriteArticle(
                id = article.id,
                title = article.title,
                author = article.author,
                score = article.score,
                time = article.time,
                url = article.url,
                dateAdded = System.currentTimeMillis()
            )
            localDataSource.toggleFavourite(favouriteArticle)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
