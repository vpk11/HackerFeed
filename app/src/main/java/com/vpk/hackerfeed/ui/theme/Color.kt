package com.vpk.hackerfeed.ui.theme

import androidx.compose.ui.graphics.Color

// --- Solarized Light & Space Gray Theme Colors ---

// Space Gray Dark Theme Colors
val SpaceGrayBackground = Color(0xFF202124)     // Main background
val SpaceGrayCard = Color(0xFF292A2D)          // Card backgrounds, surfaces
val SpaceGrayText = Color(0xFFE0E0E0)          // Primary text
val SpaceGraySecondaryText = Color(0xFFB0B0B0) // Secondary text
val SpaceGrayPrimary = Color(0xFF00BFAE)       // Primary accent (teal)
val SpaceGraySecondary = Color(0xFFFFB300)     // Secondary accent (amber)
val SpaceGrayBorder = Color(0xFF3C3C3F)        // Borders and dividers

// Solarized Light Theme Colors
val SolarizedBackground = Color(0xFFFDF6E3)    // Main background
val SolarizedCard = Color(0xFFEEE8D5)          // Card backgrounds, surfaces
val SolarizedText = Color(0xFF657B83)          // Primary text (gray-blue)
val SolarizedSecondaryText = Color(0xFF839496) // Secondary text
val SolarizedPrimary = Color(0xFF268BD2)       // Primary accent (soft blue)
val SolarizedSecondary = Color(0xFFB58900)     // Secondary accent (golden yellow)
val SolarizedBorder = Color(0xFFD3D0C8)        // Borders and dividers

// Legacy GitHub colors (keeping for backward compatibility)
val GithubDarkCharcoal = SpaceGrayBackground
val GithubDarkGray = SpaceGrayCard
val GithubMediumGray = SpaceGrayBorder
val GithubLightGray = SpaceGrayText
val GithubDimGray = SpaceGraySecondaryText
val GithubCardBackgroundDark = SpaceGrayCard

val GithubBlue = SpaceGrayPrimary
val GithubGreen = Color(0xFF3FB950)        // Keep for success states
val GithubPurple = Color(0xFFBC8EFF)       // Keep for other accents

val GithubOffWhite = SolarizedBackground
val GithubLightSurfaceGray = SolarizedCard
val GithubLightBorderGray = SolarizedBorder
val GithubDarkText = SolarizedText
val GithubSecondaryText = SolarizedSecondaryText
val GithubCardBackgroundLight = SolarizedCard

// Original Material Defaults (can be kept for reference or other themes)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)