package com.vpk.hackerfeed.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteArticleDao {
    
    @Query("SELECT * FROM favourite_articles ORDER BY dateAdded DESC")
    fun getAllFavourites(): Flow<List<FavouriteArticle>>
    
    @Query("SELECT * FROM favourite_articles WHERE id = :id")
    suspend fun getFavouriteById(id: Long): FavouriteArticle?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favourite_articles WHERE id = :id)")
    suspend fun isFavourite(id: Long): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(article: FavouriteArticle)
    
    @Query("DELETE FROM favourite_articles WHERE id = :id")
    suspend fun deleteFavouriteById(id: Long)
    
    @Delete
    suspend fun deleteFavourite(article: FavouriteArticle)
}
