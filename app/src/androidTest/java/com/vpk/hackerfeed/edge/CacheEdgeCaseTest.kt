package com.vpk.hackerfeed.edge

import androidx.test.core.app.ApplicationProvider
import com.vpk.hackerfeed.data.cache.CacheConfig
import com.vpk.hackerfeed.data.cache.CacheManager
import com.vpk.hackerfeed.database.AppDatabase
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.helpers.TestHelpers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheEdgeCaseTest {

    private lateinit var db: AppDatabase
    private lateinit var cacheManager: CacheManager

    @Before
    fun setup() {
        db = TestHelpers.createInMemoryDb(ApplicationProvider.getApplicationContext())
        cacheManager = CacheManager(db.cachedArticleDao(), db.cachedTopStoriesDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun expiredCache_returnsNull() = runTest {
        val expiredTimestamp = TestData.Timestamps.hoursAgo(hours = 2)
        db.cachedArticleDao().insertCachedArticle(
            TestData.cachedArticle(cachedAt = expiredTimestamp)
        )

        val result = cacheManager.getCachedArticle(TestData.Ids.ARTICLE_1)

        assertNull(result)
    }

    @Test
    fun clearExpiredCache_preservesValidEntries() = runTest {
        val validArticle = TestData.cachedArticle(id = TestData.Ids.ARTICLE_1)
        val expiredArticle = TestData.cachedArticle(
            id = TestData.Ids.ARTICLE_2,
            cachedAt = TestData.Timestamps.hoursAgo(hours = 2)
        )
        db.cachedArticleDao().insertCachedArticle(validArticle)
        db.cachedArticleDao().insertCachedArticle(expiredArticle)

        cacheManager.clearExpiredCache()

        assertNotNull(db.cachedArticleDao().getCachedArticle(TestData.Ids.ARTICLE_1))
        assertNull(db.cachedArticleDao().getCachedArticle(TestData.Ids.ARTICLE_2))
    }

    @Test
    fun cacheExceedingMax_triggersCleanup() = runTest {
        val overLimit = CacheConfig.MAX_CACHED_ARTICLES + 5
        val articles = (1L..overLimit).map { id ->
            TestData.article(id = id, title = "Article $id")
        }
        articles.forEach { cacheManager.cacheArticle(it) }

        val count = db.cachedArticleDao().getCachedArticleCount()

        assertTrue(
            "Cache count $count should be <= ${CacheConfig.MAX_CACHED_ARTICLES}",
            count <= CacheConfig.MAX_CACHED_ARTICLES
        )
    }

    @Test
    fun emptyCache_clearAll_doesNotCrash() = runTest {
        cacheManager.clearAllCache()

        assertNull(cacheManager.getCachedTopStories())
        assertEquals(0, db.cachedArticleDao().getCachedArticleCount())
    }

    @Test
    fun emptyCache_clearExpired_doesNotCrash() = runTest {
        cacheManager.clearExpiredCache()

        assertEquals(0, db.cachedArticleDao().getCachedArticleCount())
    }

    @Test
    fun topStoriesMemoryCache_invalidatedAfterClearAll() = runTest {
        cacheManager.cacheTopStories(TestData.Ids.TOP_STORIES)
        assertNotNull(cacheManager.getCachedTopStories())

        cacheManager.clearAllCache()

        assertNull(cacheManager.getCachedTopStories())
    }
}
