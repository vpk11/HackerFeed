package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.domain.model.Article

/**
 * Data source interface for remote news operations.
 * This abstracts the remote data source implementation.
 */
interface RemoteNewsDataSource {
    suspend fun getTopStoryIds(): List<Long>
    suspend fun getArticleDetails(id: Long): Article
}
