package com.vpk.hackerfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.database.AppDatabase
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

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val favouritesRepository = FavouritesRepository(database.favouriteArticleDao())

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
