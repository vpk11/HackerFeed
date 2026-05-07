package com.vpk.hackerfeed.database

import androidx.test.core.app.ApplicationProvider
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

class CachedArticleDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: CachedArticleDao

    @Before
    fun setup() {
        db = TestHelpers.createInMemoryDb(ApplicationProvider.getApplicationContext())
        dao = db.cachedArticleDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetCachedArticle() = runTest {
        val article = TestData.cachedArticle()

        dao.insertCachedArticle(article)

        val retrieved = dao.getCachedArticle(article.id)
        assertNotNull(retrieved)
        assertEquals(article.id, retrieved!!.id)
        assertEquals(article.title, retrieved.title)
        assertEquals(article.author, retrieved.author)
    }

    @Test
    fun getCachedArticleReturnsNullForMissing() = runTest {
        assertNull(dao.getCachedArticle(TestData.Ids.ARTICLE_1))
    }

    @Test
    fun insertBatchAndGetMultiple() = runTest {
        val articles = listOf(
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_1),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_2),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_3)
        )
        dao.insertCachedArticles(articles)

        val ids = listOf(TestData.Ids.ARTICLE_1, TestData.Ids.ARTICLE_2)
        val retrieved = dao.getCachedArticles(ids)

        assertEquals(2, retrieved.size)
    }

    @Test
    fun deleteExpiredArticlesRemovesOnlyExpired() = runTest {
        val expired = TestData.cachedArticle(
            id = TestData.Ids.ARTICLE_1,
            cachedAt = TestData.Timestamps.hoursAgo(hours = 2)
        )
        val valid = TestData.cachedArticle(
            id = TestData.Ids.ARTICLE_2,
            cachedAt = TestData.Timestamps.NOW
        )
        dao.insertCachedArticle(expired)
        dao.insertCachedArticle(valid)

        val cutoff = TestData.Timestamps.hoursAgo(hours = 1)
        dao.deleteExpiredArticles(cutoff)

        assertNull(dao.getCachedArticle(expired.id))
        assertNotNull(dao.getCachedArticle(valid.id))
    }

    @Test
    fun getCachedArticleCount() = runTest {
        val articles = listOf(
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_1),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_2),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_3)
        )
        dao.insertCachedArticles(articles)

        assertEquals(3, dao.getCachedArticleCount())
    }

    @Test
    fun getRecentCachedArticlesRespectsLimit() = runTest {
        val articles = listOf(
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_1, cachedAt = TestData.Timestamps.minutesAgo(minutes = 3)),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_2, cachedAt = TestData.Timestamps.minutesAgo(minutes = 2)),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_3, cachedAt = TestData.Timestamps.minutesAgo(minutes = 1))
        )
        dao.insertCachedArticles(articles)

        val limit = 2
        val recent = dao.getRecentCachedArticles(limit)

        assertEquals(limit, recent.size)
        assertEquals(TestData.Ids.ARTICLE_3, recent[0].id)
        assertEquals(TestData.Ids.ARTICLE_2, recent[1].id)
    }

    @Test
    fun replaceAllCachedArticlesClearsAndInserts() = runTest {
        dao.insertCachedArticle(TestData.cachedArticle(id = TestData.Ids.ARTICLE_1))

        val replacements = listOf(
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_4),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_5)
        )
        dao.replaceAllCachedArticles(replacements)

        assertNull(dao.getCachedArticle(TestData.Ids.ARTICLE_1))
        assertNotNull(dao.getCachedArticle(TestData.Ids.ARTICLE_4))
        assertNotNull(dao.getCachedArticle(TestData.Ids.ARTICLE_5))
        assertEquals(2, dao.getCachedArticleCount())
    }

    @Test
    fun clearAllCachedArticles() = runTest {
        dao.insertCachedArticles(listOf(
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_1),
            TestData.cachedArticle(id = TestData.Ids.ARTICLE_2)
        ))

        dao.clearAllCachedArticles()

        assertEquals(0, dao.getCachedArticleCount())
    }
}
