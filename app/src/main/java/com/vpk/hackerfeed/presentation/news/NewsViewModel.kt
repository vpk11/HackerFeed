package com.vpk.hackerfeed.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.usecase.GetArticleDetailsUseCase
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.GetTopStoriesUseCase
import com.vpk.hackerfeed.domain.usecase.ToggleFavouriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = true,
    val storyIds: List<Long> = emptyList(),
    val articles: Map<Long, Article?> = emptyMap(),
    val favouriteArticleIds: Set<Long> = emptySet(),
    val error: String? = null
)

class NewsViewModel(
    private val getTopStoriesUseCase: GetTopStoriesUseCase,
    private val getArticleDetailsUseCase: GetArticleDetailsUseCase,
    private val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    // Pull to refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchTopStories()
        observeFavourites()
    }

    private fun observeFavourites() {
        viewModelScope.launch {
            getFavouriteArticlesUseCase().collect { favourites ->
                _uiState.update { 
                    it.copy(favouriteArticleIds = favourites.map { fav -> fav.id }.toSet()) 
                }
            }
        }
    }

    private fun fetchTopStories(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            
            getTopStoriesUseCase().fold(
                onSuccess = { ids ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            storyIds = ids,
                            articles = if (isRefresh) emptyMap() else it.articles,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = "Failed to load stories: ${exception.message}"
                        ) 
                    }
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
        _uiState.update { it.copy(error = null) }
        fetchTopStories(isRefresh = true)
    }

    fun fetchArticleDetails(id: Long) {
        if (_uiState.value.articles[id] != null && !_isRefreshing.value) return

        viewModelScope.launch {
            getArticleDetailsUseCase(id).fold(
                onSuccess = { article ->
                    _uiState.update { currentState ->
                        val updatedArticles = currentState.articles.toMutableMap()
                        updatedArticles[id] = article
                        currentState.copy(articles = updatedArticles)
                    }
                },
                onFailure = { exception ->
                    _uiState.update { currentState ->
                        val updatedArticles = currentState.articles.toMutableMap()
                        updatedArticles[id] = null // Mark as failed/not loaded
                        currentState.copy(
                            articles = updatedArticles,
                            error = "Failed to load article $id: ${exception.message}"
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
                    _uiState.update { 
                        it.copy(error = "Failed to toggle favourite: ${exception.message}") 
                    }
                }
            )
        }
    }
}
