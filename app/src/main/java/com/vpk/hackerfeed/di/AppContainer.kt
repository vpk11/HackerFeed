package com.vpk.hackerfeed.di

import android.app.Application
import com.vpk.hackerfeed.database.AppDatabase
import com.vpk.hackerfeed.repository.FavouritesRepository

/**
 * Manual dependency injection container for the application.
 * This provides dependencies to ViewModels and other components.
 */
interface AppContainer {
    val favouritesRepository: FavouritesRepository
}

/**
 * Default implementation of AppContainer that provides real dependencies.
 */
class DefaultAppContainer(
    private val application: Application
) : AppContainer {
    
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }
    
    override val favouritesRepository: FavouritesRepository by lazy {
        FavouritesRepository(database.favouriteArticleDao())
    }
}
