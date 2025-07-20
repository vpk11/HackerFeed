package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.database.FavouriteArticleDao
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of LocalFavouritesDataSource using Room database.
 * This handles all local database operations for favourite articles.
 */
class RoomFavouritesDataSource(
    private val dao: FavouriteArticleDao
) : LocalFavouritesDataSource {
    
    override fun getAllFavourites(): Flow<List<FavouriteArticle>> {
        return dao.getAllFavourites().map { entities ->
            entities.map { entity ->
                FavouriteArticle(
                    id = entity.id,
                    title = entity.title,
                    author = entity.author,
                    score = entity.score,
                    time = entity.time,
                    url = entity.url,
                    dateAdded = entity.dateAdded
                )
            }
        }
    }
    
    override suspend fun getFavouriteById(id: Long): FavouriteArticle? {
        val entity = dao.getFavouriteById(id)
        return entity?.let {
            FavouriteArticle(
                id = it.id,
                title = it.title,
                author = it.author,
                score = it.score,
                time = it.time,
                url = it.url,
                dateAdded = it.dateAdded
            )
        }
    }
    
    override suspend fun isFavourite(id: Long): Boolean {
        return dao.isFavourite(id)
    }
    
    override suspend fun insertFavourite(article: FavouriteArticle) {
        val entity = com.vpk.hackerfeed.database.FavouriteArticle(
            id = article.id,
            title = article.title,
            author = article.author,
            score = article.score,
            time = article.time,
            url = article.url,
            dateAdded = article.dateAdded
        )
        dao.insertFavourite(entity)
    }
    
    override suspend fun deleteFavouriteById(id: Long) {
        dao.deleteFavouriteById(id)
    }
    
    override suspend fun toggleFavourite(article: FavouriteArticle) {
        val entity = com.vpk.hackerfeed.database.FavouriteArticle(
            id = article.id,
            title = article.title,
            author = article.author,
            score = article.score,
            time = article.time,
            url = article.url,
            dateAdded = article.dateAdded
        )
        dao.toggle(entity)
    }
}
