package com.vpk.hackerfeed.presentation.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.data.provider.StringResourceProvider
import com.vpk.hackerfeed.domain.usecase.ClearCacheUseCase
import com.vpk.hackerfeed.domain.usecase.ClearExpiredCacheUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CacheUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

/**
 * ViewModel for cache management operations.
 * Provides functionality to clear cache and view cache status.
 */
class CacheViewModel(
    private val clearCacheUseCase: ClearCacheUseCase,
    private val clearExpiredCacheUseCase: ClearExpiredCacheUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CacheUiState())
    val uiState: StateFlow<CacheUiState> = _uiState.asStateFlow()

    /**
     * Clears all cached data.
     */
    fun clearAllCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, message = null) }
            
            clearCacheUseCase().fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = stringResourceProvider.getString(R.string.cache_cleared_successfully),
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = stringResourceProvider.getString(R.string.failed_to_clear_cache, exception.message ?: ""),
                            message = null
                        )
                    }
                }
            )
        }
    }

    /**
     * Clears only expired cache entries.
     */
    fun clearExpiredCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, message = null) }
            
            clearExpiredCacheUseCase().fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = stringResourceProvider.getString(R.string.expired_cache_cleared_successfully),
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = stringResourceProvider.getString(R.string.failed_to_clear_expired_cache, exception.message ?: ""),
                            message = null
                        )
                    }
                }
            )
        }
    }

    /**
     * Clears the current message/error state.
     */
    fun clearMessage() {
        _uiState.update { it.copy(message = null, error = null) }
    }
    
    /**
     * Clears only the message state.
     */
    fun clearMessageOnly() {
        _uiState.update { it.copy(message = null) }
    }
    
    /**
     * Clears only the error state.
     */
    fun clearErrorOnly() {
        _uiState.update { it.copy(error = null) }
    }
}
