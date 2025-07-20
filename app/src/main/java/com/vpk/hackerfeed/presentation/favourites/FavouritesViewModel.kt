package com.vpk.hackerfeed.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.RemoveFromFavouritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavouritesUiState(
    val isLoading: Boolean = true,
    val favouriteArticles: List<FavouriteArticle> = emptyList(),
    val error: String? = null
)

class FavouritesViewModel(
    private val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                getFavouriteArticlesUseCase().collect { favourites ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favouriteArticles = favourites,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load favourites: ${e.message}"
                )
            }
        }
    }

    fun removeFromFavourites(articleId: Long) {
        viewModelScope.launch {
            removeFromFavouritesUseCase(articleId).fold(
                onSuccess = {
                    // Success - the UI state will be updated through the Flow
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to remove favourite: ${exception.message}"
                    )
                }
            )
        }
    }
}
