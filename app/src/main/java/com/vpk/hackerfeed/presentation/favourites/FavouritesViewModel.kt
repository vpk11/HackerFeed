package com.vpk.hackerfeed.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.data.provider.StringResourceProvider
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.RemoveFromFavouritesUseCase
import com.vpk.hackerfeed.presentation.base.BaseUiState
import com.vpk.hackerfeed.presentation.base.UiStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavouritesUiState(
    override val isLoading: Boolean = true,
    val favouriteArticles: List<FavouriteArticle> = emptyList(),
    override val error: String? = null
) : BaseUiState

class FavouritesViewModel(
    private val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val uiStateManager = UiStateManager(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = uiStateManager.uiState

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                getFavouriteArticlesUseCase().collect { favourites ->
                    uiStateManager.setSuccess {
                        it.copy(
                            isLoading = false,
                            favouriteArticles = favourites,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                uiStateManager.setError {
                    it.copy(
                        isLoading = false,
                        error = stringResourceProvider.getString(R.string.failed_to_load_favourites, e.message ?: "")
                    )
                }
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
                    uiStateManager.setError {
                        it.copy(
                            error = stringResourceProvider.getString(R.string.failed_to_remove_favourite, exception.message ?: "")
                        )
                    }
                }
            )
        }
    }
}
