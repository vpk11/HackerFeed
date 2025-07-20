package com.vpk.hackerfeed.presentation.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base UI state interface that provides common state properties
 * used across different screens in the app.
 */
interface BaseUiState {
    val isLoading: Boolean
    val error: String?
}

/**
 * A utility class for managing common UI state patterns.
 * Provides helper methods for loading, error, and success states.
 *
 * @param T The specific UI state type that extends BaseUiState
 */
class UiStateManager<T : BaseUiState>(initialState: T) {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    /**
     * Sets the loading state
     */
    fun setLoading(updateState: (T) -> T) {
        _uiState.update(updateState)
    }

    /**
     * Sets an error state
     */
    fun setError(updateState: (T) -> T) {
        _uiState.update(updateState)
    }

    /**
     * Sets a success state
     */
    fun setSuccess(updateState: (T) -> T) {
        _uiState.update(updateState)
    }

    /**
     * Updates the state with any custom logic
     */
    fun updateState(updateState: (T) -> T) {
        _uiState.update(updateState)
    }

    /**
     * Gets the current state value
     */
    val currentState: T get() = _uiState.value
}

/**
 * Extension function to safely handle results and update UI state accordingly.
 * This pattern is commonly used across ViewModels for handling use case results.
 *
 * @param result The Result to handle
 * @param onSuccess Called when result is success
 * @param onError Called when result is failure, provides error message
 */
inline fun <T, R : BaseUiState> UiStateManager<R>.handleResult(
    result: Result<T>,
    crossinline onSuccess: (T) -> R,
    crossinline onError: (String) -> R
) {
    result.fold(
        onSuccess = { data ->
            setSuccess { onSuccess(data) }
        },
        onFailure = { exception ->
            setError { onError(exception.message ?: "Unknown error") }
        }
    )
}
