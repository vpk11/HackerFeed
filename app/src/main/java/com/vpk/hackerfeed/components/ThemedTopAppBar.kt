package com.vpk.hackerfeed.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.ui.theme.ElectricCyan

/**
 * A cyberpunk-themed TopAppBar with translucent background and neon bottom border.
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
    val neonBorderColor = ElectricCyan.copy(alpha = 0.6f)

    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = Modifier
            .drawBehind {
                // Neon bottom border line
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = neonBorderColor,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            },
        colors = colors ?: if (isDarkTheme) {
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.85f),
                titleContentColor = ElectricCyan,
                navigationIconContentColor = ElectricCyan,
                actionIconContentColor = ElectricCyan
            )
        } else {
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFE8E8EC).copy(alpha = 0.9f),
                titleContentColor = Color(0xFF00CCCC),
                navigationIconContentColor = Color(0xFF00CCCC),
                actionIconContentColor = Color(0xFF00CCCC)
            )
        }
    )
}
