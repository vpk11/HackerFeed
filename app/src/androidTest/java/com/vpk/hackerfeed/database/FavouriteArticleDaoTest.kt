package com.vpk.hackerfeed.database

import androidx.test.core.app.ApplicationProvider
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.helpers.TestHelpers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavouriteArticleDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: FavouriteArticleDao

    @Before
    fun setup() {
        db = TestHelpers.createInMemoryDb(ApplicationProvider.getApplicationContext())
        dao = db.favouriteArticleDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndRetrieveFavourite() = runTest {
        val article = TestData.dbFavouriteArticle()

        dao.insertFavourite(article)

        val retrieved = dao.getFavouriteById(article.id)
        assertNotNull(retrieved)
        assertEquals(article.id, retrieved!!.id)
        assertEquals(article.title, retrieved.title)
        assertEquals(article.author, retrieved.author)
    }

    @Test
    fun deleteFavouriteById() = runTest {
        val article = TestData.dbFavouriteArticle()
        dao.insertFavourite(article)

        dao.deleteFavouriteById(article.id)

        assertNull(dao.getFavouriteById(article.id))
    }

    @Test
    fun isFavouriteReturnsTrueForExisting() = runTest {
        val article = TestData.dbFavouriteArticle()
        dao.insertFavourite(article)

        assertTrue(dao.isFavourite(article.id))
    }

    @Test
    fun isFavouriteReturnsFalseForNonExisting() = runTest {
        assertFalse(dao.isFavourite(TestData.Ids.ARTICLE_1))
    }

    @Test
    fun toggleAddsWhenNotFavourite() = runTest {
        val article = TestData.dbFavouriteArticle()

        dao.toggle(article)

        assertTrue(dao.isFavourite(article.id))
    }

    @Test
    fun toggleRemovesWhenAlreadyFavourite() = runTest {
        val article = TestData.dbFavouriteArticle()
        dao.insertFavourite(article)

        dao.toggle(article)

        assertFalse(dao.isFavourite(article.id))
    }

    @Test
    fun getAllFavouritesEmitsFlowOrderedByDateAddedDesc() = runTest {
        val older = TestData.dbFavouriteArticle(
            id = TestData.Ids.ARTICLE_1,
            dateAdded = TestData.Timestamps.minutesAgo(minutes = 10)
        )
        val newer = TestData.dbFavouriteArticle(
            id = TestData.Ids.ARTICLE_2,
            dateAdded = TestData.Timestamps.NOW
        )
        dao.insertFavourite(older)
        dao.insertFavourite(newer)

        val favourites = dao.getAllFavourites().first()

        assertEquals(2, favourites.size)
        assertEquals(newer.id, favourites[0].id)
        assertEquals(older.id, favourites[1].id)
    }

    @Test
    fun getAllFavouritesEmitsEmptyListInitially() = runTest {
        val favourites = dao.getAllFavourites().first()

        assertTrue(favourites.isEmpty())
    }

    @Test
    fun insertWithConflictReplaces() = runTest {
        val original = TestData.dbFavouriteArticle(title = "Original")
        dao.insertFavourite(original)

        val updated = TestData.dbFavouriteArticle(title = "Updated")
        dao.insertFavourite(updated)

        val retrieved = dao.getFavouriteById(original.id)
        assertEquals("Updated", retrieved!!.title)
    }
}
