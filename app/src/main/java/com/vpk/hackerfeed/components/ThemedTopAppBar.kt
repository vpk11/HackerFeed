package com.vpk.hackerfeed.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.vpk.hackerfeed.ui.theme.GithubDarkGray

/**
 * A themed TopAppBar that automatically adapts its colors based on the system theme.
 * This ensures consistent theming across all activities in the app.
 * 
 * @param title The title content for the TopAppBar
 * @param navigationIcon The navigation icon content (usually a back button)
 * @param actions Optional action buttons for the TopAppBar
 * @param colors Optional custom colors - if not provided, will use the app's standard theme colors
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors? = null
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors ?: if (isDarkTheme) {
            TopAppBarDefaults.topAppBarColors(
                containerColor = GithubDarkGray,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        } else {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}
