package com.vpk.hackerfeed.data.repository

import com.vpk.hackerfeed.data.datasource.RemoteNewsDataSource
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.repository.NewsRepository

/**
 * Implementation of NewsRepository.
 * This class coordinates between different data sources for news operations.
 */
class NewsRepositoryImpl(
    private val remoteDataSource: RemoteNewsDataSource
) : NewsRepository {
    
    override suspend fun getTopStoryIds(): Result<List<Long>> {
        return try {
            val storyIds = remoteDataSource.getTopStoryIds()
            Result.success(storyIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getArticleDetails(id: Long): Result<Article> {
        return try {
            val article = remoteDataSource.getArticleDetails(id)
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
