package com.vpk.hackerfeed

import android.app.Application
import com.vpk.hackerfeed.di.AppContainer
import com.vpk.hackerfeed.di.DefaultAppContainer

/**
 * Custom Application class that provides the dependency injection container.
 */
class HackerFeedApplication : Application() {
    
    // Lazy initialization of the DI container
    val container: AppContainer by lazy {
        DefaultAppContainer(this)
    }
}
