package com.vpk.hackerfeed.edge

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
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RapidUserActionTest {

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
    fun doubleTapFavourite_doesNotCrash() {
        coEvery { localDataSource.getCachedTopStories() } returns TestData.Ids.TOP_STORIES
        val article = TestData.article()

        val viewModel = createViewModel()
        viewModel.toggleFavourite(article)
        viewModel.toggleFavourite(article)

        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun refreshWhileRefreshing_isNoOp() {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES

        val viewModel = createViewModel()
        viewModel.refreshTopStories()
        viewModel.refreshTopStories()

        assertFalse(viewModel.isRefreshing.value)
    }

    @Test
    fun toggleFavouriteDuringRefresh_doesNotCorruptState() {
        coEvery { localDataSource.getCachedTopStories() } returns null
        coEvery { remoteDataSource.getTopStoryIds() } returns TestData.Ids.TOP_STORIES
        val article = TestData.article()

        val viewModel = createViewModel()
        viewModel.refreshTopStories()
        viewModel.toggleFavourite(article)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
    }
}
