package com.vpk.hackerfeed.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import com.vpk.hackerfeed.R
import kotlinx.coroutines.launch

@Composable
fun AnimatedFavoriteButton(
    isFavourite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Animated color transition
    val iconColor by animateColorAsState(
        targetValue = if (isFavourite) 
            MaterialTheme.colorScheme.primary 
        else 
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 200),
        label = "favorite_color_animation"
    )

    IconButton(
        onClick = {
            // Trigger the bounce animation
            coroutineScope.launch {
                // Scale down
                scale.animateTo(
                    targetValue = 0.7f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                )
                // Scale up with bounce
                scale.animateTo(
                    targetValue = 1.3f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                // Return to normal size
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
    ) {
        Icon(
            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavourite) 
                stringResource(R.string.remove_from_favourites) 
            else 
                stringResource(R.string.add_to_favourites),
            tint = iconColor,
            modifier = Modifier.scale(scale.value)
        )
    }
}
