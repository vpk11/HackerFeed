package com.vpk.hackerfeed.di

import android.app.Application
import com.vpk.hackerfeed.RetrofitInstance
import com.vpk.hackerfeed.database.AppDatabase
import com.vpk.hackerfeed.repository.FavouritesRepository
import com.vpk.hackerfeed.repository.NewsRepository

/**
 * Manual dependency injection container for the application.
 * This provides dependencies to ViewModels and other components.
 */
interface AppContainer {
    val favouritesRepository: FavouritesRepository
    val newsRepository: NewsRepository
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
    
    override val newsRepository: NewsRepository by lazy {
        NewsRepository(RetrofitInstance.api)
    }
}
