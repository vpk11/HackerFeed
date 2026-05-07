package com.vpk.hackerfeed.data.datasource

import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.StoryType

/**
 * Data source interface for remote news operations.
 * This abstracts the remote data source implementation.
 */
interface RemoteNewsDataSource {
    suspend fun getStoryIds(storyType: StoryType = StoryType.TOP): List<Long>
    suspend fun getArticleDetails(id: Long): Article
}
