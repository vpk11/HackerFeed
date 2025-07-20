package com.vpk.hackerfeed.repository

import com.vpk.hackerfeed.Article
import com.vpk.hackerfeed.HackerNewsApiService

class NewsRepository(private val apiService: HackerNewsApiService) {
    
    suspend fun getTopStoryIds(): List<Long> = apiService.getTopStoryIds()
    
    suspend fun getArticleDetails(id: Long): Article = apiService.getArticleDetails(id)
}
