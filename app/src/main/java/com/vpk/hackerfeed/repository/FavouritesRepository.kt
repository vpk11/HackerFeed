package com.vpk.hackerfeed.repository

import com.vpk.hackerfeed.Article
import com.vpk.hackerfeed.database.FavouriteArticle
import com.vpk.hackerfeed.database.FavouriteArticleDao
import kotlinx.coroutines.flow.Flow

class FavouritesRepository(private val dao: FavouriteArticleDao) {
    
    fun getAllFavourites(): Flow<List<FavouriteArticle>> = dao.getAllFavourites()
    
    suspend fun isFavourite(articleId: Long): Boolean = dao.isFavourite(articleId)
    
    suspend fun addToFavourites(article: Article) {
        val favouriteArticle = FavouriteArticle(
            id = article.id,
            title = article.title,
            author = article.author,
            score = article.score,
            time = article.time,
            url = article.url
        )
        dao.insertFavourite(favouriteArticle)
    }
    
    suspend fun removeFromFavourites(articleId: Long) {
        dao.deleteFavouriteById(articleId)
    }
    
    suspend fun toggleFavourite(article: Article) {
        if (isFavourite(article.id)) {
            removeFromFavourites(article.id)
        } else {
            addToFavourites(article)
        }
    }
}
