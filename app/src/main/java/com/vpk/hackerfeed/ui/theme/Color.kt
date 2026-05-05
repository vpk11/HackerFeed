package com.vpk.hackerfeed.ui.theme

import androidx.compose.ui.graphics.Color

// --- Cyberpunk / Neon Theme Colors ---

// Primary Accents
val ElectricCyan = Color(0xFF00FFFF)
val HotMagenta = Color(0xFFFF00FF)

// Dark Theme Palette
val CyberpunkDarkBackground = Color(0xFF0A0A0F)       // Deep black/charcoal
val CyberpunkDarkSurface = Color(0xFF12121A)           // Card backgrounds
val CyberpunkDarkSurfaceVariant = Color(0xFF1A1A25)    // Elevated surfaces
val CyberpunkDarkText = Color(0xFFE0E8F0)              // Primary text
val CyberpunkDarkSecondaryText = Color(0xFFA0A8B8)     // Secondary text
val CyberpunkDarkBorder = Color(0xFF00FFFF).copy(alpha = 0.3f) // Neon cyan border
val CyberpunkDarkDivider = Color(0xFF1E1E2A)           // Subtle dividers

// Light Theme Palette
val CyberpunkLightBackground = Color(0xFFE8E8EC)       // Cool light gray
val CyberpunkLightSurface = Color(0xFFF0F0F4)          // Card backgrounds
val CyberpunkLightSurfaceVariant = Color(0xFFE0E0E8)   // Elevated surfaces
val CyberpunkLightText = Color(0xFF0A0A1A)             // Primary text (dark)
val CyberpunkLightSecondaryText = Color(0xFF404060)    // Secondary text
val CyberpunkLightBorder = Color(0xFF00CCCC).copy(alpha = 0.5f) // Slightly muted cyan border
val CyberpunkLightDivider = Color(0xFFD0D0D8)          // Subtle dividers

// Neon Error / Success / Accent
val NeonRed = Color(0xFFFF003C)           // Error states
val NeonGreen = Color(0xFF00FF66)         // Success states
val NeonYellow = Color(0xFFFFFF00)        // Warning states

// Glow / Translucent helpers
val CyanGlow = Color(0x4000FFFF)          // Soft cyan glow (25% alpha)
val MagentaGlow = Color(0x40FF00FF)       // Soft magenta glow (25% alpha)
