package com.vpk.hackerfeed.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.data.provider.StringResourceProvider
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.usecase.GetArticleDetailsUseCase
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.GetTopStoriesUseCase
import com.vpk.hackerfeed.domain.usecase.ToggleFavouriteUseCase
import com.vpk.hackerfeed.domain.usecase.ClearExpiredCacheUseCase
import com.vpk.hackerfeed.presentation.base.BaseUiState
import com.vpk.hackerfeed.presentation.base.UiStateManager
import com.vpk.hackerfeed.presentation.base.handleResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    override val isLoading: Boolean = true,
    val storyIds: List<Long> = emptyList(),
    val articles: Map<Long, Article?> = emptyMap(),
    val favouriteArticleIds: Set<Long> = emptySet(),
    override val error: String? = null
) : BaseUiState

class NewsViewModel(
    private val getTopStoriesUseCase: GetTopStoriesUseCase,
    private val getArticleDetailsUseCase: GetArticleDetailsUseCase,
    private val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val clearExpiredCacheUseCase: ClearExpiredCacheUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val uiStateManager = UiStateManager(NewsUiState())
    val uiState: StateFlow<NewsUiState> = uiStateManager.uiState

    // Pull to refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchTopStories()
        observeFavourites()
        cleanupExpiredCache()
    }
    
    /**
     * Cleans up expired cache entries on ViewModel initialization.
     * This helps maintain cache hygiene without impacting user experience.
     */
    private fun cleanupExpiredCache() {
        viewModelScope.launch {
            try {
                clearExpiredCacheUseCase()
            } catch (e: Exception) {
                // Silently ignore cache cleanup errors to avoid disrupting the user experience
            }
        }
    }

    private fun observeFavourites() {
        viewModelScope.launch {
            getFavouriteArticlesUseCase().collect { favourites ->
                uiStateManager.updateState { 
                    it.copy(favouriteArticleIds = favourites.map { fav -> fav.id }.toSet()) 
                }
            }
        }
    }

    private fun fetchTopStories(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                uiStateManager.setLoading { it.copy(isLoading = true, error = null) }
            }
            
            uiStateManager.handleResult(
                result = getTopStoriesUseCase(forceRefresh = isRefresh),
                onSuccess = { ids ->
                    uiStateManager.currentState.copy(
                        isLoading = false,
                        storyIds = ids,
                        articles = if (isRefresh) emptyMap() else uiStateManager.currentState.articles,
                        error = null
                    )
                },
                onError = { errorMessage ->
                    uiStateManager.currentState.copy(
                        isLoading = false, 
                        error = stringResourceProvider.getString(R.string.failed_to_load_stories, errorMessage)
                    )
                }
            )
            
            if (isRefresh) {
                _isRefreshing.value = false
            }
        }
    }

    fun refreshTopStories() {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        uiStateManager.updateState { it.copy(error = null) }
        fetchTopStories(isRefresh = true)
    }

    fun fetchArticleDetails(id: Long) {
        // During refresh, we want to force refresh articles as well to get fresh data
        val shouldForceRefresh = _isRefreshing.value
        
        if (uiStateManager.currentState.articles[id] != null && !shouldForceRefresh) return

        viewModelScope.launch {
            getArticleDetailsUseCase(id, forceRefresh = shouldForceRefresh).fold(
                onSuccess = { article ->
                    uiStateManager.updateState { currentState ->
                        val updatedArticles = currentState.articles.toMutableMap()
                        updatedArticles[id] = article
                        currentState.copy(articles = updatedArticles)
                    }
                },
                onFailure = { exception ->
                    uiStateManager.updateState { currentState ->
                        val updatedArticles = currentState.articles.toMutableMap()
                        updatedArticles[id] = null // Mark as failed/not loaded
                        currentState.copy(
                            articles = updatedArticles,
                            error = stringResourceProvider.getString(R.string.failed_to_load_article, id, exception.message ?: "")
                        )
                    }
                }
            )
        }
    }

    fun toggleFavourite(article: Article) {
        viewModelScope.launch {
            toggleFavouriteUseCase(article).fold(
                onSuccess = {
                    // Success - the UI state will be updated through observeFavourites()
                },
                onFailure = { exception ->
                    uiStateManager.setError { 
                        it.copy(error = stringResourceProvider.getString(R.string.failed_to_toggle_favourite, exception.message ?: "")) 
                    }
                }
            )
        }
    }
}
