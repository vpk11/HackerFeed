package com.vpk.hackerfeed.presentation.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.data.provider.StringResourceProvider
import com.vpk.hackerfeed.domain.usecase.ClearCacheUseCase
import com.vpk.hackerfeed.domain.usecase.ClearExpiredCacheUseCase
import com.vpk.hackerfeed.presentation.base.BaseUiState
import com.vpk.hackerfeed.presentation.base.UiStateManager
import com.vpk.hackerfeed.presentation.base.handleResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CacheUiState(
    override val isLoading: Boolean = false,
    val message: String? = null,
    override val error: String? = null
) : BaseUiState

/**
 * ViewModel for cache management operations.
 * Provides functionality to clear cache and view cache status.
 */
class CacheViewModel(
    private val clearCacheUseCase: ClearCacheUseCase,
    private val clearExpiredCacheUseCase: ClearExpiredCacheUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val uiStateManager = UiStateManager(CacheUiState())
    val uiState: StateFlow<CacheUiState> = uiStateManager.uiState

    /**
     * Clears all cached data.
     */
    fun clearAllCache() {
        viewModelScope.launch {
            uiStateManager.setLoading { it.copy(isLoading = true, error = null, message = null) }
            
            uiStateManager.handleResult(
                result = clearCacheUseCase(),
                onSuccess = {
                    uiStateManager.currentState.copy(
                        isLoading = false,
                        message = stringResourceProvider.getString(R.string.cache_cleared_successfully),
                        error = null
                    )
                },
                onError = { errorMessage ->
                    uiStateManager.currentState.copy(
                        isLoading = false,
                        error = stringResourceProvider.getString(R.string.failed_to_clear_cache, errorMessage),
                        message = null
                    )
                }
            )
        }
    }

    /**
     * Clears only expired cache entries.
     */
    fun clearExpiredCache() {
        viewModelScope.launch {
            uiStateManager.setLoading { it.copy(isLoading = true, error = null, message = null) }
            
            uiStateManager.handleResult(
                result = clearExpiredCacheUseCase(),
                onSuccess = {
                    uiStateManager.currentState.copy(
                        isLoading = false,
                        message = stringResourceProvider.getString(R.string.expired_cache_cleared_successfully),
                        error = null
                    )
                },
                onError = { errorMessage ->
                    uiStateManager.currentState.copy(
                        isLoading = false,
                        error = stringResourceProvider.getString(R.string.failed_to_clear_expired_cache, errorMessage),
                        message = null
                    )
                }
            )
        }
    }

    /**
     * Clears only the message state.
     */
    fun clearMessageOnly() {
        uiStateManager.updateState { it.copy(message = null) }
    }
    
    /**
     * Clears only the error state.
     */
    fun clearErrorOnly() {
        uiStateManager.updateState { it.copy(error = null) }
    }
}
