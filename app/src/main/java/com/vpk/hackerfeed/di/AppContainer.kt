package com.vpk.hackerfeed.di

import android.app.Application
import com.vpk.hackerfeed.data.RetrofitInstance
import com.vpk.hackerfeed.data.datasource.HackerNewsRemoteDataSource
import com.vpk.hackerfeed.data.datasource.LocalFavouritesDataSource
import com.vpk.hackerfeed.data.datasource.RemoteNewsDataSource
import com.vpk.hackerfeed.data.datasource.RoomFavouritesDataSource
import com.vpk.hackerfeed.data.repository.FavouritesRepositoryImpl
import com.vpk.hackerfeed.data.repository.NewsRepositoryImpl
import com.vpk.hackerfeed.database.AppDatabase
import com.vpk.hackerfeed.domain.repository.FavouritesRepository
import com.vpk.hackerfeed.domain.repository.NewsRepository
import com.vpk.hackerfeed.domain.usecase.GetArticleDetailsUseCase
import com.vpk.hackerfeed.domain.usecase.GetFavouriteArticlesUseCase
import com.vpk.hackerfeed.domain.usecase.GetTopStoriesUseCase
import com.vpk.hackerfeed.domain.usecase.RemoveFromFavouritesUseCase
import com.vpk.hackerfeed.domain.usecase.ToggleFavouriteUseCase

/**
 * Manual dependency injection container for the application.
 * This provides dependencies to ViewModels and other components following clean architecture.
 */
interface AppContainer {
    // Use Cases
    val getTopStoriesUseCase: GetTopStoriesUseCase
    val getArticleDetailsUseCase: GetArticleDetailsUseCase
    val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase
    val toggleFavouriteUseCase: ToggleFavouriteUseCase
    val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
}

/**
 * Default implementation of AppContainer that provides real dependencies.
 */
class DefaultAppContainer(
    private val application: Application
) : AppContainer {
    
    // Database
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(application)
    }
    
    // Data Sources
    private val remoteNewsDataSource: RemoteNewsDataSource by lazy {
        HackerNewsRemoteDataSource(RetrofitInstance.api)
    }
    
    private val localFavouritesDataSource: LocalFavouritesDataSource by lazy {
        RoomFavouritesDataSource(database.favouriteArticleDao())
    }
    
    // Repositories
    private val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(remoteNewsDataSource)
    }
    
    private val favouritesRepository: FavouritesRepository by lazy {
        FavouritesRepositoryImpl(localFavouritesDataSource)
    }
    
    // Use Cases
    override val getTopStoriesUseCase: GetTopStoriesUseCase by lazy {
        GetTopStoriesUseCase(newsRepository)
    }
    
    override val getArticleDetailsUseCase: GetArticleDetailsUseCase by lazy {
        GetArticleDetailsUseCase(newsRepository)
    }
    
    override val getFavouriteArticlesUseCase: GetFavouriteArticlesUseCase by lazy {
        GetFavouriteArticlesUseCase(favouritesRepository)
    }
    
    override val toggleFavouriteUseCase: ToggleFavouriteUseCase by lazy {
        ToggleFavouriteUseCase(favouritesRepository)
    }
    
    override val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase by lazy {
        RemoveFromFavouritesUseCase(favouritesRepository)
    }
}
