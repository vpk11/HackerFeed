package com.vpk.hackerfeed.database

import androidx.test.core.app.ApplicationProvider
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.helpers.TestHelpers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CachedTopStoriesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: CachedTopStoriesDao

    @Before
    fun setup() {
        db = TestHelpers.createInMemoryDb(ApplicationProvider.getApplicationContext())
        dao = db.cachedTopStoriesDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetCachedTopStories() = runTest {
        val topStories = TestData.cachedTopStories()

        dao.insertCachedTopStories(topStories)

        val retrieved = dao.getCachedTopStories()
        assertNotNull(retrieved)
        assertEquals(TestData.Ids.TOP_STORIES, retrieved!!.storyIds)
    }

    @Test
    fun getCachedTopStoriesReturnsNullWhenEmpty() = runTest {
        assertNull(dao.getCachedTopStories())
    }

    @Test
    fun clearCachedTopStories() = runTest {
        dao.insertCachedTopStories(TestData.cachedTopStories())

        dao.clearCachedTopStories()

        assertNull(dao.getCachedTopStories())
    }

    @Test
    fun insertReplacesExisting() = runTest {
        val original = TestData.cachedTopStories(
            storyIds = listOf(TestData.Ids.ARTICLE_1, TestData.Ids.ARTICLE_2)
        )
        dao.insertCachedTopStories(original)

        val replacement = TestData.cachedTopStories(
            storyIds = listOf(TestData.Ids.ARTICLE_3, TestData.Ids.ARTICLE_4, TestData.Ids.ARTICLE_5)
        )
        dao.insertCachedTopStories(replacement)

        val retrieved = dao.getCachedTopStories()
        assertEquals(replacement.storyIds, retrieved!!.storyIds)
    }

    @Test
    fun cachedAtTimestampIsPreserved() = runTest {
        val fixedTimestamp = TestData.Timestamps.EPOCH_RECENT
        val topStories = TestData.cachedTopStories(cachedAt = fixedTimestamp)

        dao.insertCachedTopStories(topStories)

        val retrieved = dao.getCachedTopStories()
        assertEquals(fixedTimestamp, retrieved!!.cachedAt)
    }
}
