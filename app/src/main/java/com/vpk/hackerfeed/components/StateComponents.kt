package com.vpk.hackerfeed.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.ui.theme.ElectricCyan
import com.vpk.hackerfeed.ui.theme.HotMagenta

/**
 * Neon scanning ring loader — glowing cyan trail on a circular path.
 */
@Composable
fun LoadingStateComponent(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon_scan")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        ),
        label = "sweep_rotation"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier.size(56.dp)) {
                val strokeWidth = 4.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2

                // Dim background ring
                drawCircle(
                    color = ElectricCyan.copy(alpha = 0.15f),
                    radius = radius,
                    style = Stroke(width = strokeWidth)
                )

                // Glowing cyan sweep arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Transparent,
                            ElectricCyan.copy(alpha = 0.3f),
                            ElectricCyan.copy(alpha = 0.7f),
                            ElectricCyan
                        )
                    ),
                    startAngle = sweepAngle,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            if (message != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ElectricCyan.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Holographic shimmer empty state — icon with animated gradient overlay.
 */
@Composable
fun EmptyStateComponent(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "holographic_shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon with holographic shimmer overlay
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = ElectricCyan.copy(alpha = 0.5f)
                )
                // Shimmer overlay using Canvas
                Canvas(modifier = Modifier.size(64.dp)) {
                    val shimmerBrush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            ElectricCyan.copy(alpha = 0.3f),
                            HotMagenta.copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        start = Offset(size.width * shimmerOffset, 0f),
                        end = Offset(size.width * (shimmerOffset + 0.5f), size.height)
                    )
                    drawRect(brush = shimmerBrush)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 'SIGNAL LOST' glitch error text — layered text composables with
 * cyan/magenta channel split offsets.
 */
@Composable
fun ErrorStateComponent(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitch")
    val glitchOffsetX by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glitch_x"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Glitch "SIGNAL LOST" header
            Box {
                // Cyan channel (offset left)
                Text(
                    text = "[ SIGNAL LOST ]",
                    style = MaterialTheme.typography.headlineSmall,
                    color = ElectricCyan.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = (glitchOffsetX + 2).dp)
                )
                // Magenta channel (offset right)
                Text(
                    text = "[ SIGNAL LOST ]",
                    style = MaterialTheme.typography.headlineSmall,
                    color = HotMagenta.copy(alpha = 0.5f),
                    modifier = Modifier.padding(end = (glitchOffsetX + 2).dp)
                )
                // White primary layer
                Text(
                    text = "[ SIGNAL LOST ]",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}
