package com.vpk.hackerfeed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpk.hackerfeed.FavouritesViewModel
import com.vpk.hackerfeed.NewsViewModel

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
                    container.newsRepository,
                    container.favouritesRepository
                ) as T
            }
            FavouritesViewModel::class.java -> {
                FavouritesViewModel(container.favouritesRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
