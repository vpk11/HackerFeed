package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.data.HackerNewsApiService
import com.vpk.hackerfeed.domain.model.Article

/**
 * Implementation of RemoteNewsDataSource using HackerNews API.
 * This handles all remote network operations for news data.
 */
class HackerNewsRemoteDataSource(
    private val apiService: HackerNewsApiService
) : RemoteNewsDataSource {
    
    override suspend fun getTopStoryIds(): List<Long> {
        return apiService.getTopStoryIds()
    }
    
    override suspend fun getArticleDetails(id: Long): Article {
        val apiArticle = apiService.getArticleDetails(id)
        return Article(
            id = apiArticle.id,
            author = apiArticle.author,
            score = apiArticle.score,
            time = apiArticle.time,
            title = apiArticle.title,
            url = apiArticle.url
        )
    }
}
