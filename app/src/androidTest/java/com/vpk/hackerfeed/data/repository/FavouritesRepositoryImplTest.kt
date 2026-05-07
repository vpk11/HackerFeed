package com.vpk.hackerfeed.data.repository

import com.vpk.hackerfeed.data.datasource.LocalFavouritesDataSource
import com.vpk.hackerfeed.helpers.TestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavouritesRepositoryImplTest {

    private lateinit var localDataSource: LocalFavouritesDataSource
    private lateinit var repository: FavouritesRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        repository = FavouritesRepositoryImpl(localDataSource)
    }

    @Test
    fun getAllFavourites_emitsFromDataSource() = runTest {
        val favourites = listOf(
            TestData.favouriteArticle(id = TestData.Ids.ARTICLE_1),
            TestData.favouriteArticle(id = TestData.Ids.ARTICLE_2)
        )
        every { localDataSource.getAllFavourites() } returns flowOf(favourites)

        val result = repository.getAllFavourites().first()

        assertEquals(2, result.size)
        assertEquals(TestData.Ids.ARTICLE_1, result[0].id)
    }

    @Test
    fun addToFavourites_insertsViaDataSource() = runTest {
        val article = TestData.article()

        val result = repository.addToFavourites(article)

        assertTrue(result.isSuccess)
        coVerify { localDataSource.insertFavourite(any()) }
    }

    @Test
    fun removeFromFavourites_deletesViaDataSource() = runTest {
        val result = repository.removeFromFavourites(TestData.Ids.ARTICLE_1)

        assertTrue(result.isSuccess)
        coVerify { localDataSource.deleteFavouriteById(TestData.Ids.ARTICLE_1) }
    }

    @Test
    fun toggleFavourite_delegatesToDataSource() = runTest {
        val article = TestData.article()

        val result = repository.toggleFavourite(article)

        assertTrue(result.isSuccess)
        coVerify { localDataSource.toggleFavourite(any()) }
    }

    @Test
    fun isFavourite_returnsDataSourceValue() = runTest {
        coEvery { localDataSource.isFavourite(TestData.Ids.ARTICLE_1) } returns true
        coEvery { localDataSource.isFavourite(TestData.Ids.ARTICLE_2) } returns false

        assertTrue(repository.isFavourite(TestData.Ids.ARTICLE_1))
        assertFalse(repository.isFavourite(TestData.Ids.ARTICLE_2))
    }

    @Test
    fun addToFavourites_onException_returnsFailure() = runTest {
        coEvery { localDataSource.insertFavourite(any()) } throws RuntimeException("db error")

        val result = repository.addToFavourites(TestData.article())

        assertTrue(result.isFailure)
    }

    @Test
    fun toggleFavourite_addThenRemove() = runTest {
        val article = TestData.article()

        repository.toggleFavourite(article)
        repository.toggleFavourite(article)

        coVerify(exactly = 2) { localDataSource.toggleFavourite(any()) }
    }
}
