package com.vpk.hackerfeed.data.cache

import androidx.test.core.app.ApplicationProvider
import com.vpk.hackerfeed.database.AppDatabase
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.helpers.TestHelpers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CacheManagerTest {

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
    fun cacheAndGetTopStories_memoryCacheHit() = runTest {
        cacheManager.cacheTopStories(TestData.Ids.TOP_STORIES)

        val result = cacheManager.getCachedTopStories()

        assertEquals(TestData.Ids.TOP_STORIES, result)
    }

    @Test
    fun getCachedTopStories_dbCacheHit() = runTest {
        db.cachedTopStoriesDao().insertCachedTopStories(TestData.cachedTopStories())

        val result = cacheManager.getCachedTopStories()

        assertEquals(TestData.Ids.TOP_STORIES, result)
    }

    @Test
    fun getCachedTopStories_expired_returnsNull() = runTest {
        val expiredTimestamp = TestData.Timestamps.minutesAgo(minutes = 10)
        db.cachedTopStoriesDao().insertCachedTopStories(
            TestData.cachedTopStories(cachedAt = expiredTimestamp)
        )

        val result = cacheManager.getCachedTopStories()

        assertNull(result)
    }

    @Test
    fun cacheArticle_roundTrip() = runTest {
        val article = TestData.article()

        cacheManager.cacheArticle(article)

        val cached = cacheManager.getCachedArticle(article.id)
        assertNotNull(cached)
        assertEquals(article.id, cached!!.id)
        assertEquals(article.title, cached.title)
        assertEquals(article.author, cached.author)
    }

    @Test
    fun getCachedArticle_expired_returnsNull() = runTest {
        val expiredArticle = TestData.cachedArticle(
            cachedAt = TestData.Timestamps.hoursAgo(hours = 2)
        )
        db.cachedArticleDao().insertCachedArticle(expiredArticle)

        val result = cacheManager.getCachedArticle(expiredArticle.id)

        assertNull(result)
    }

    @Test
    fun getCachedArticles_filtersExpired() = runTest {
        val validArticle = TestData.cachedArticle(id = TestData.Ids.ARTICLE_1)
        val expiredArticle = TestData.cachedArticle(
            id = TestData.Ids.ARTICLE_2,
            cachedAt = TestData.Timestamps.hoursAgo(hours = 2)
        )
        db.cachedArticleDao().insertCachedArticle(validArticle)
        db.cachedArticleDao().insertCachedArticle(expiredArticle)

        val ids = listOf(TestData.Ids.ARTICLE_1, TestData.Ids.ARTICLE_2)
        val result = cacheManager.getCachedArticles(ids)

        assertEquals(1, result.size)
        assertNotNull(result[TestData.Ids.ARTICLE_1])
    }

    @Test
    fun clearAllCache_clearsMemoryAndDb() = runTest {
        cacheManager.cacheTopStories(TestData.Ids.TOP_STORIES)
        cacheManager.cacheArticle(TestData.article())

        cacheManager.clearAllCache()

        assertNull(cacheManager.getCachedTopStories())
        assertNull(cacheManager.getCachedArticle(TestData.Ids.ARTICLE_1))
    }

    @Test
    fun clearExpiredCache_removesOnlyExpired() = runTest {
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
    fun performCacheCleanupIfNeeded_trimsToMax() = runTest {
        val maxArticles = CacheConfig.MAX_CACHED_ARTICLES
        val articles = (1L..maxArticles + 5).map { id ->
            TestData.article(id = id, title = "Article $id")
        }
        articles.forEach { cacheManager.cacheArticle(it) }

        val count = db.cachedArticleDao().getCachedArticleCount()

        assert(count <= maxArticles) {
            "Cache count $count should be <= $maxArticles after cleanup"
        }
    }
}
