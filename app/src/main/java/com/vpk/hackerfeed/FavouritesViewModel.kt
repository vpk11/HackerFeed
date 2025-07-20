package com.vpk.hackerfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.database.FavouriteArticle
import com.vpk.hackerfeed.repository.FavouritesRepository
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
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                favouritesRepository.getAllFavourites().collect { favourites ->
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
            favouritesRepository.removeFromFavourites(articleId)
        }
    }
}
