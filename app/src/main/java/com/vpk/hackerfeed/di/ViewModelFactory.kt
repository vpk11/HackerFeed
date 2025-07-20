package com.vpk.hackerfeed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpk.hackerfeed.presentation.favourites.FavouritesViewModel
import com.vpk.hackerfeed.presentation.news.NewsViewModel
import com.vpk.hackerfeed.presentation.cache.CacheViewModel

/**
 * Custom ViewModelFactory that uses dependency injection container to provide dependencies.
 */
class ViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NewsViewModel::class.java -> {
                NewsViewModel(
                    getTopStoriesUseCase = container.getTopStoriesUseCase,
                    getArticleDetailsUseCase = container.getArticleDetailsUseCase,
                    getFavouriteArticlesUseCase = container.getFavouriteArticlesUseCase,
                    toggleFavouriteUseCase = container.toggleFavouriteUseCase,
                    clearExpiredCacheUseCase = container.clearExpiredCacheUseCase
                ) as T
            }
            FavouritesViewModel::class.java -> {
                FavouritesViewModel(
                    getFavouriteArticlesUseCase = container.getFavouriteArticlesUseCase,
                    removeFromFavouritesUseCase = container.removeFromFavouritesUseCase
                ) as T
            }
            CacheViewModel::class.java -> {
                CacheViewModel(
                    clearCacheUseCase = container.clearCacheUseCase,
                    clearExpiredCacheUseCase = container.clearExpiredCacheUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
