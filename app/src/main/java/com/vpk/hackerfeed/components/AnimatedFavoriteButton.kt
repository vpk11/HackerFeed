package com.vpk.hackerfeed.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.ui.theme.ElectricCyan
import com.vpk.hackerfeed.ui.theme.HotMagenta
import kotlinx.coroutines.launch

@Composable
fun AnimatedFavoriteButton(
    isFavourite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    // Neon color shift: cyan for filled, magenta outline for unfilled
    val iconColor by animateColorAsState(
        targetValue = if (isFavourite) ElectricCyan else HotMagenta.copy(alpha = 0.7f),
        animationSpec = tween(durationMillis = 300),
        label = "favorite_color_animation"
    )

    // Pulsing glow animation for the filled state
    val infiniteTransition = rememberInfiniteTransition(label = "glow_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ),
        label = "glow_alpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ),
        label = "pulse_scale"
    )

    val glowColor = if (isFavourite) ElectricCyan.copy(alpha = glowAlpha) else Color.Transparent
    val effectiveScale = if (isFavourite) scale.value * pulseScale else scale.value

    IconButton(
        onClick = {
            coroutineScope.launch {
                // Bounce animation
                scale.animateTo(
                    targetValue = 0.7f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                )
                scale.animateTo(
                    targetValue = 1.3f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            onClick()
        },
        modifier = modifier
            .drawBehind {
                // Neon glow circle behind the heart
                if (isFavourite) {
                    drawCircle(
                        color = glowColor,
                        radius = size.minDimension * 0.6f,
                        center = Offset(size.width / 2, size.height / 2)
                    )
                }
            }
    ) {
        Icon(
            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavourite)
                stringResource(R.string.remove_from_favourites)
            else
                stringResource(R.string.add_to_favourites),
            tint = iconColor,
            modifier = Modifier.scale(effectiveScale)
        )
    }
}
