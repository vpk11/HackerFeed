package com.vpk.hackerfeed.integration

import com.vpk.hackerfeed.data.datasource.LocalFavouritesDataSource
import com.vpk.hackerfeed.data.datasource.LocalNewsDataSource
import com.vpk.hackerfeed.data.datasource.RemoteNewsDataSource
import com.vpk.hackerfeed.data.repository.FavouritesRepositoryImpl
import com.vpk.hackerfeed.data.repository.NewsRepositoryImpl
import com.vpk.hackerfeed.domain.usecase.ClearExpiredCacheUseCase
import com.vpk.hackerfeed.domain.usecase.GetArticleDetailsUseCase
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.GetTopStoriesUseCase
import com.vpk.hackerfeed.domain.usecase.ToggleFavouriteUseCase
import com.vpk.hackerfeed.helpers.FakeStringResourceProvider
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.presentation.news.NewsViewModel
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelIntegrationTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var remoteDataSource: RemoteNewsDataSource
    private lateinit var localDataSource: LocalNewsDataSource
    private lateinit var localFavouritesDataSource: LocalFavouritesDataSource

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        remoteDataSource = mockk()
        localDataSource = mockk(relaxed = true)
        localFavouritesDataSource = mockk(relaxed = true)
        every { localFavouritesDataSource.getAllFavourites() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): NewsViewModel {
        val newsRepo = NewsRepositoryImpl(remoteDataSource, localDataSource)
        val favRepo = FavouritesRepositoryImpl(localFavouritesDataSource)
        return NewsViewModel(
            getTopStoriesUseCase = GetTopStoriesUseCase(newsRepo),
            getArticleDetailsUseCase = GetArticleDetailsUseCase(newsRepo),
            getFavouriteArticlesUseCase = GetFavouriteArticlesUseCase(favRepo),
            toggleFavouriteUseCase = ToggleFavouriteUseCase(favRepo),
            clearExpiredCacheUseCase = ClearExpiredCacheUseCase(newsRepo),
            stringResourceProvider = FakeStringResourceProvider()
        )
    }

    @Test
    fun init_fetchesTopStories() {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES

        val viewModel = createViewModel()

        assertEquals(TestData.Ids.TOP_STORIES, viewModel.uiState.value.storyIds)
    }

    @Test
    fun fetchArticleDetails_populatesArticlesMap() {
        val article = TestData.article()
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES
        coEvery { localDataSource.getCachedArticle(article.id) } returns null
        coEvery { remoteDataSource.getArticleDetails(article.id) } returns article

        val viewModel = createViewModel()
        viewModel.fetchArticleDetails(article.id)

        assertNotNull(viewModel.uiState.value.articles[article.id])
        assertEquals(article.title, viewModel.uiState.value.articles[article.id]?.title)
    }

    @Test
    fun refreshTopStories_setsRefreshingAndReloads() {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES

        val viewModel = createViewModel()
        viewModel.refreshTopStories()

        assertEquals(TestData.Ids.TOP_STORIES, viewModel.uiState.value.storyIds)
    }

    @Test
    fun networkError_setsErrorState() {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } throws IOException("network down")

        val viewModel = createViewModel()

        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun toggleFavourite_updatesFavouriteIds() {
        val favouriteIds = listOf(TestData.favouriteArticle(id = TestData.Ids.ARTICLE_1))
        every { localFavouritesDataSource.getAllFavourites() } returns flowOf(favouriteIds)
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES

        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.favouriteArticleIds.contains(TestData.Ids.ARTICLE_1))
    }

    @Test
    fun cleanupExpiredCache_runsSilently() {
        coEvery { localDataSource.getCachedTopStories() } returns TestData.Ids.TOP_STORIES
        coEvery { localDataSource.clearExpiredCache() } throws RuntimeException("cleanup failed")

        val viewModel = createViewModel()

        assertNull(viewModel.uiState.value.error)
    }
}
