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
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getFavouriteById(id: Long): FavouriteArticle? {
        val entity = dao.getFavouriteById(id)
        return entity?.toDomainModel()
    }
    
    override suspend fun isFavourite(id: Long): Boolean {
        return dao.isFavourite(id)
    }
    
    override suspend fun insertFavourite(article: FavouriteArticle) {
        dao.insertFavourite(article.toEntity())
    }
    
    override suspend fun deleteFavouriteById(id: Long) {
        dao.deleteFavouriteById(id)
    }
    
    override suspend fun toggleFavourite(article: FavouriteArticle) {
        dao.toggle(article.toEntity())
    }
}

// Extension functions for mapping between database entity and domain model
private fun com.vpk.hackerfeed.database.FavouriteArticle.toDomainModel(): FavouriteArticle {
    return FavouriteArticle(
        id = this.id,
        title = this.title,
        author = this.author,
        score = this.score,
        time = this.time,
        url = this.url,
        dateAdded = this.dateAdded
    )
}

private fun FavouriteArticle.toEntity(): com.vpk.hackerfeed.database.FavouriteArticle {
    return com.vpk.hackerfeed.database.FavouriteArticle(
        id = this.id,
        title = this.title,
        author = this.author,
        score = this.score,
        time = this.time,
        url = this.url,
        dateAdded = this.dateAdded
    )
}
