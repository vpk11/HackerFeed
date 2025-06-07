package com.vpk.hackerfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = true,
    val storyIds: List<Long> = emptyList(),
    val articles: Map<Long, Article?> = emptyMap(),
    val error: String? = null
)

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    // Pull to request state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchTopStories()
    }

    private fun fetchTopStories(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            try {
                val ids = RetrofitInstance.api.getTopStoryIds()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        storyIds = ids,
                        articles = if (isRefresh) emptyMap() else it.articles
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load stories: ${e.message}") }
            }  finally {
                if (isRefresh) {
                    _isRefreshing.value = false
                }
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
        if (_uiState.value.articles.containsKey(id) && _uiState.value.articles[id] != null && !_isRefreshing.value) return

        viewModelScope.launch {
            try {
                if (_isRefreshing.value && !_uiState.value.articles.containsKey(id)) {
                    _uiState.update { currentState ->
                        val updatedArticles = currentState.articles.toMutableMap()
                        updatedArticles[id] = null
                        currentState.copy(articles = updatedArticles)
                    }
                }
                val article = RetrofitInstance.api.getArticleDetails(id)
                _uiState.update { currentState ->
                    val updatedArticles = currentState.articles.toMutableMap()
                    updatedArticles[id] = article
                    currentState.copy(articles = updatedArticles)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load article $id: ${e.message}") }
            }
        }
    }
}