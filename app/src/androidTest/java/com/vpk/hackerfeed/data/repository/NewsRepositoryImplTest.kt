package com.vpk.hackerfeed.data.repository

import com.vpk.hackerfeed.data.datasource.LocalNewsDataSource
import com.vpk.hackerfeed.data.datasource.RemoteNewsDataSource
import com.vpk.hackerfeed.domain.model.StoryType
import com.vpk.hackerfeed.helpers.TestData

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NewsRepositoryImplTest {

    private lateinit var remoteDataSource: RemoteNewsDataSource
    private lateinit var localDataSource: LocalNewsDataSource
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk(relaxed = true)
        repository = NewsRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun getTopStoryIds_cacheHit_returnsLocal() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns TestData.Ids.TOP_STORIES

        val result = repository.getStoryIds()

        assertTrue(result.isSuccess)
        assertEquals(TestData.Ids.TOP_STORIES, result.getOrNull())
        coVerify(exactly = 0) { remoteDataSource.getStoryIds(match { true }) }
    }

    @Test
    fun getTopStoryIds_cacheMiss_fetchesRemoteAndCaches() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getStoryIds(match { true }) } returns TestData.Ids.TOP_STORIES

        val result = repository.getStoryIds()

        assertTrue(result.isSuccess)
        assertEquals(TestData.Ids.TOP_STORIES, result.getOrNull())
        coVerify { localDataSource.cacheTopStories(TestData.Ids.TOP_STORIES) }
    }

    @Test
    fun getTopStoryIds_forceRefresh_bypassesCache() = runTest {
        coEvery { remoteDataSource.getStoryIds(match { true }) } returns TestData.Ids.TOP_STORIES

        val result = repository.getStoryIds(forceRefresh = true)

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { localDataSource.getCachedTopStories() }
    }

    @Test
    fun getTopStoryIds_networkError_fallsBackToExpiredCache() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns null andThen TestData.Ids.TOP_STORIES
        coEvery { remoteDataSource.getStoryIds(match { true }) } throws IOException("network down")

        val result = repository.getStoryIds()

        assertTrue(result.isSuccess)
        assertEquals(TestData.Ids.TOP_STORIES, result.getOrNull())
    }

    @Test
    fun getTopStoryIds_networkErrorNoCacheFallback_returnsFailure() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getStoryIds(match { true }) } throws IOException("network down")

        val result = repository.getStoryIds()

        assertTrue(result.isFailure)
    }

    @Test
    fun getArticleDetails_cacheHit_returnsLocal() = runTest {
        val article = TestData.article()
        coEvery { localDataSource.getCachedArticle(article.id) } returns article

        val result = repository.getArticleDetails(article.id)

        assertTrue(result.isSuccess)
        assertEquals(article, result.getOrNull())
    }

    @Test
    fun getArticleDetails_cacheMiss_fetchesRemoteAndCaches() = runTest {
        val article = TestData.article()
        coEvery { localDataSource.getCachedArticle(article.id) } returns null
        coEvery { remoteDataSource.getArticleDetails(article.id) } returns article

        val result = repository.getArticleDetails(article.id)

        assertTrue(result.isSuccess)
        coVerify { localDataSource.cacheArticle(article) }
    }

    @Test
    fun getStoryIds_passesStoryTypeToRemote() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getStoryIds(StoryType.NEW) } returns TestData.Ids.TOP_STORIES

        repository.getStoryIds(storyType = StoryType.NEW)

        coVerify { remoteDataSource.getStoryIds(StoryType.NEW) }
    }

    @Test
    fun getStoryIds_bestType_passesToRemote() = runTest {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getStoryIds(StoryType.BEST) } returns TestData.Ids.TOP_STORIES

        val result = repository.getStoryIds(storyType = StoryType.BEST)

        assertTrue(result.isSuccess)
        coVerify { remoteDataSource.getStoryIds(StoryType.BEST) }
    }

    @Test
    fun clearCache_delegatesToLocal() = runTest {
        val result = repository.clearCache()

        assertTrue(result.isSuccess)
        coVerify { localDataSource.clearCache() }
    }

    @Test
    fun clearExpiredCache_delegatesToLocal() = runTest {
        val result = repository.clearExpiredCache()

        assertTrue(result.isSuccess)
        coVerify { localDataSource.clearExpiredCache() }
    }
}
