package com.vpk.hackerfeed.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_articles")
data class FavouriteArticle(
    @PrimaryKey val id: Long,
    val title: String?,
    val author: String?,
    val score: Int?,
    val time: Long?,
    val url: String?,
    val dateAdded: Long = System.currentTimeMillis()
)
