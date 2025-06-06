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
    val articles: Map<Long, Article> = emptyMap(),
    val error: String? = null
)

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        fetchTopStories()
    }

    private fun fetchTopStories() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val ids = RetrofitInstance.api.getTopStoryIds()
                _uiState.update { it.copy(isLoading = false, storyIds = ids) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load stories: ${e.message}") }
            }
        }
    }

    fun fetchArticleDetails(id: Long) {
        if (_uiState.value.articles.containsKey(id)) return

        viewModelScope.launch {
            try {
                val article = RetrofitInstance.api.getArticleDetails(id)
                _uiState.update { currentState ->
                    val updatedArticles = currentState.articles.toMutableMap()
                    updatedArticles[id] = article
                    currentState.copy(articles = updatedArticles)
                }
            } catch (e: Exception) {
            }
        }
    }
}