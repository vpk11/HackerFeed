package com.vpk.hackerfeed.ui.theme

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

private val CyberpunkDarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = Color.Black,
    primaryContainer = ElectricCyan.copy(alpha = 0.15f),
    onPrimaryContainer = CyberpunkDarkText,

    secondary = HotMagenta,
    onSecondary = Color.Black,
    secondaryContainer = HotMagenta.copy(alpha = 0.20f),
    onSecondaryContainer = CyberpunkDarkText,

    tertiary = NeonGreen,
    onTertiary = Color.Black,
    tertiaryContainer = NeonGreen.copy(alpha = 0.15f),
    onTertiaryContainer = CyberpunkDarkText,

    error = NeonRed,
    onError = Color.White,
    errorContainer = NeonRed.copy(alpha = 0.15f),
    onErrorContainer = CyberpunkDarkText,

    background = CyberpunkDarkBackground,
    onBackground = CyberpunkDarkText,

    surface = CyberpunkDarkSurface,
    onSurface = CyberpunkDarkText,

    surfaceVariant = CyberpunkDarkSurfaceVariant,
    onSurfaceVariant = CyberpunkDarkSecondaryText,

    outline = CyberpunkDarkBorder,
    inverseOnSurface = CyberpunkDarkBackground,
    inverseSurface = CyberpunkDarkText,
    inversePrimary = ElectricCyan.copy(alpha = 0.8f),
    surfaceTint = ElectricCyan.copy(alpha = 0.08f),
    outlineVariant = ElectricCyan.copy(alpha = 0.15f),
    scrim = Color.Black.copy(alpha = 0.7f)
)

private val CyberpunkLightColorScheme = lightColorScheme(
    primary = Color(0xFF00CCCC),           // Slightly darker cyan for readability on light bg
    onPrimary = Color.White,
    primaryContainer = ElectricCyan.copy(alpha = 0.15f),
    onPrimaryContainer = CyberpunkLightText,

    secondary = Color(0xFFCC00CC),         // Slightly darker magenta for readability
    onSecondary = Color.White,
    secondaryContainer = HotMagenta.copy(alpha = 0.15f),
    onSecondaryContainer = CyberpunkLightText,

    tertiary = NeonGreen,
    onTertiary = Color.Black,
    tertiaryContainer = NeonGreen.copy(alpha = 0.1f),
    onTertiaryContainer = CyberpunkLightText,

    error = NeonRed,
    onError = Color.White,
    errorContainer = NeonRed.copy(alpha = 0.1f),
    onErrorContainer = CyberpunkLightText,

    background = CyberpunkLightBackground,
    onBackground = CyberpunkLightText,

    surface = CyberpunkLightSurface,
    onSurface = CyberpunkLightText,

    surfaceVariant = CyberpunkLightSurfaceVariant,
    onSurfaceVariant = CyberpunkLightSecondaryText,

    outline = CyberpunkLightBorder,
    inverseOnSurface = CyberpunkLightSurface,
    inverseSurface = CyberpunkLightText,
    inversePrimary = ElectricCyan.copy(alpha = 0.9f),
    surfaceTint = ElectricCyan.copy(alpha = 0.05f),
    outlineVariant = ElectricCyan.copy(alpha = 0.2f),
    scrim = Color.Black.copy(alpha = 0.4f)
)

@Composable
fun HackerFeedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CyberpunkDarkColorScheme
        else -> CyberpunkLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HackerFeedTypography,
        content = content
    )
}
