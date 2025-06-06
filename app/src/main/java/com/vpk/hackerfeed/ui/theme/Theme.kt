package com.vpk.hackerfeed.ui.theme // Ensure this is your correct package

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val GithubDarkColorScheme = darkColorScheme(
    primary = GithubBlue,
    onPrimary = Color.White, // Text on GithubBlue buttons/elements
    primaryContainer = GithubBlue.copy(alpha = 0.3f), // Can be used for larger highlighted areas
    onPrimaryContainer = GithubLightGray,

    secondary = GithubPurple, // A secondary accent if needed
    onSecondary = Color.Black,
    secondaryContainer = GithubPurple.copy(alpha = 0.2f),
    onSecondaryContainer = GithubLightGray,

    tertiary = GithubGreen, // A tertiary accent (e.g., for success states)
    onTertiary = Color.Black,
    tertiaryContainer = GithubGreen.copy(alpha = 0.2f),
    onTertiaryContainer = GithubLightGray,

    error = Color(0xFFF85149), // GitHub's red for errors
    onError = Color.White,
    errorContainer = Color(0xFFDA3633).copy(alpha = 0.2f),
    onErrorContainer = GithubLightGray,

    background = GithubDarkCharcoal,
    onBackground = GithubLightGray, // Main text color

    surface = GithubDarkGray,         // Cards, AppBar background
    onSurface = GithubLightGray,      // Text on cards/AppBar

    surfaceVariant = GithubMediumGray,  // For elements like highlighted menu items or input field backgrounds
    onSurfaceVariant = GithubDimGray,   // Text on surface variants

    outline = GithubMediumGray,       // Borders for cards, text fields
    inverseOnSurface = GithubDarkCharcoal,
    inverseSurface = GithubLightGray,
    inversePrimary = GithubBlue.copy(alpha = 0.8f), // Often for pressed states of primary elements
    surfaceTint = GithubBlue.copy(alpha = 0.1f), // Subtle tint on elevated surfaces
    outlineVariant = GithubDimGray.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.6f)
)

private val GithubLightColorScheme = lightColorScheme(
    primary = GithubBlue,
    onPrimary = Color.White,
    primaryContainer = GithubBlue.copy(alpha = 0.2f),
    onPrimaryContainer = GithubDarkText,

    secondary = GithubPurple,
    onSecondary = Color.White,
    secondaryContainer = GithubPurple.copy(alpha = 0.15f),
    onSecondaryContainer = GithubDarkText,

    tertiary = GithubGreen,
    onTertiary = Color.White,
    tertiaryContainer = GithubGreen.copy(alpha = 0.15f),
    onTertiaryContainer = GithubDarkText,

    error = Color(0xFFCF222E), // GitHub's red for light theme errors
    onError = Color.White,
    errorContainer = Color(0xFFCF222E).copy(alpha = 0.1f),
    onErrorContainer = GithubDarkText,

    background = GithubOffWhite,
    onBackground = GithubDarkText,

    surface = GithubLightSurfaceGray, // Cards, AppBar
    onSurface = GithubDarkText,

    surfaceVariant = GithubOffWhite, // Often same as background or slightly different
    onSurfaceVariant = GithubSecondaryText,

    outline = GithubLightBorderGray,
    inverseOnSurface = GithubLightSurfaceGray,
    inverseSurface = GithubDarkText,
    inversePrimary = GithubBlue.copy(alpha = 0.9f),
    surfaceTint = GithubBlue.copy(alpha = 0.05f),
    outlineVariant = GithubLightBorderGray.copy(alpha = 0.7f),
    scrim = Color.Black.copy(alpha = 0.32f)
)

@Composable
fun HackerFeedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to enforce GitHub-like theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> GithubDarkColorScheme
        else -> GithubLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // We'll use the default M3 Typography for now
        content = content
    )
}