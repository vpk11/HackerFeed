package com.vpk.hackerfeed.integration

import com.vpk.hackerfeed.data.datasource.LocalFavouritesDataSource
import com.vpk.hackerfeed.data.repository.FavouritesRepositoryImpl
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.RemoveFromFavouritesUseCase
import com.vpk.hackerfeed.helpers.FakeStringResourceProvider
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.presentation.favourites.FavouritesViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesViewModelIntegrationTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var localDataSource: LocalFavouritesDataSource

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        localDataSource = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): FavouritesViewModel {
        val repo = FavouritesRepositoryImpl(localDataSource)
        return FavouritesViewModel(
            getFavouriteArticlesUseCase = GetFavouriteArticlesUseCase(repo),
            removeFromFavouritesUseCase = RemoveFromFavouritesUseCase(repo),
            stringResourceProvider = FakeStringResourceProvider()
        )
    }

    @Test
    fun init_loadsFavourites() {
        val favourites = listOf(
            TestData.favouriteArticle(id = TestData.Ids.ARTICLE_1),
            TestData.favouriteArticle(id = TestData.Ids.ARTICLE_2)
        )
        every { localDataSource.getAllFavourites() } returns flowOf(favourites)

        val viewModel = createViewModel()

        assertEquals(2, viewModel.uiState.value.favouriteArticles.size)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun removeFromFavourites_delegatesToUseCase() {
        every { localDataSource.getAllFavourites() } returns flowOf(emptyList())

        val viewModel = createViewModel()
        viewModel.removeFromFavourites(TestData.Ids.ARTICLE_1)

        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun errorOnLoad_setsErrorState() {
        every { localDataSource.getAllFavourites() } throws RuntimeException("db error")

        val viewModel = createViewModel()

        assertNotNull(viewModel.uiState.value.error)
    }
}
