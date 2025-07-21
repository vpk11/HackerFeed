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
import com.vpk.hackerfeed.ui.theme.Typography as HackerFeedTypography

private val SpaceGrayDarkColorScheme = darkColorScheme(
    primary = SpaceGrayPrimary,
    onPrimary = Color.White,
    primaryContainer = SpaceGrayPrimary.copy(alpha = 0.3f),
    onPrimaryContainer = SpaceGrayText,

    secondary = SpaceGraySecondary,
    onSecondary = Color.Black,
    secondaryContainer = SpaceGraySecondary.copy(alpha = 0.2f),
    onSecondaryContainer = SpaceGrayText,

    tertiary = GithubGreen, // Keep green for success states
    onTertiary = Color.Black,
    tertiaryContainer = GithubGreen.copy(alpha = 0.2f),
    onTertiaryContainer = SpaceGrayText,

    error = Color(0xFFF85149),
    onError = Color.White,
    errorContainer = Color(0xFFDA3633).copy(alpha = 0.2f),
    onErrorContainer = SpaceGrayText,

    background = SpaceGrayBackground,
    onBackground = SpaceGrayText,

    surface = SpaceGrayCard,
    onSurface = SpaceGrayText,

    surfaceVariant = SpaceGrayCard,
    onSurfaceVariant = SpaceGraySecondaryText,

    outline = SpaceGrayBorder,
    inverseOnSurface = SpaceGrayBackground,
    inverseSurface = SpaceGrayText,
    inversePrimary = SpaceGrayPrimary.copy(alpha = 0.8f),
    surfaceTint = SpaceGrayPrimary.copy(alpha = 0.1f),
    outlineVariant = SpaceGrayBorder.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.6f)
)

private val SolarizedLightColorScheme = lightColorScheme(
    primary = SolarizedPrimary,
    onPrimary = Color.White,
    primaryContainer = SolarizedPrimary.copy(alpha = 0.2f),
    onPrimaryContainer = SolarizedText,

    secondary = SolarizedSecondary,
    onSecondary = Color.Black,
    secondaryContainer = SolarizedSecondary.copy(alpha = 0.15f),
    onSecondaryContainer = SolarizedText,

    tertiary = GithubGreen,
    onTertiary = Color.White,
    tertiaryContainer = GithubGreen.copy(alpha = 0.15f),
    onTertiaryContainer = SolarizedText,

    error = Color(0xFFCF222E),
    onError = Color.White,
    errorContainer = Color(0xFFCF222E).copy(alpha = 0.1f),
    onErrorContainer = SolarizedText,

    background = SolarizedBackground,
    onBackground = SolarizedText,

    surface = SolarizedCard,
    onSurface = SolarizedText,

    surfaceVariant = SolarizedCard,
    onSurfaceVariant = SolarizedSecondaryText,

    outline = SolarizedBorder,
    inverseOnSurface = SolarizedCard,
    inverseSurface = SolarizedText,
    inversePrimary = SolarizedPrimary.copy(alpha = 0.9f),
    surfaceTint = SolarizedPrimary.copy(alpha = 0.05f),
    outlineVariant = SolarizedBorder.copy(alpha = 0.7f),
    scrim = Color.Black.copy(alpha = 0.32f)
)

@Composable
fun HackerFeedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to enforce custom Solarized/Space Gray theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> SpaceGrayDarkColorScheme
        else -> SolarizedLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HackerFeedTypography, // Use the Typography from Type.kt
        content = content
    )
}