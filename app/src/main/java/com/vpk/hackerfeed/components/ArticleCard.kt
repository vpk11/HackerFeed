package com.vpk.hackerfeed.components

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.ui.theme.ElectricCyan
import com.vpk.hackerfeed.ui.theme.JetBrainsMono
import androidx.compose.foundation.isSystemInDarkTheme
import com.vpk.hackerfeed.ui.theme.ElectricViolet
import com.vpk.hackerfeed.ui.theme.ElectricVioletLight

/**
 * A cyberpunk-styled article card with glassmorphism effect (API 31+)
 * and neon border styling.
 */
@Composable
fun ArticleCard(
    article: Article?,
    modifier: Modifier = Modifier,
    isFavourite: Boolean = false,
    onToggleFavourite: () -> Unit = {},
    onLongClick: () -> Unit = {},
    showFavoriteButton: Boolean = true
) {
    val context = LocalContext.current
    val cardContentColor = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface
    val cyanGlow = ElectricCyan.copy(alpha = 0.15f)
    val buttonColor = if (isSystemInDarkTheme()) ElectricViolet else ElectricVioletLight
    val longClickInteractionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick,
                indication = null,
                interactionSource = longClickInteractionSource
            )
            // Neon glow shadow behind the card
            .drawBehind {
                drawRoundRect(
                    color = cyanGlow,
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    size = size.copy(
                        width = size.width + 4.dp.toPx(),
                        height = size.height + 4.dp.toPx()
                    )
                )
            }
            .then(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer {
                        // Glassmorphism blur on API 31+
                        renderEffect = android.graphics.RenderEffect
                            .createBlurEffect(2f, 2f, android.graphics.Shader.TileMode.CLAMP)
                            .let { null } // We skip actual blur on the card itself to keep text sharp
                        alpha = 0.97f
                    }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor.copy(alpha = 0.7f),
            contentColor = cardContentColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (article == null) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = ElectricCyan
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = article.title ?: stringResource(R.string.no_title),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = JetBrainsMono,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "by ${article.author ?: stringResource(R.string.unknown)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.points, article.score ?: 0),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        if (showFavoriteButton) {
                            AnimatedFavoriteButton(
                                isFavourite = isFavourite,
                                onClick = onToggleFavourite,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    if (article.url != null) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor.copy(alpha = 0.85f),
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text(stringResource(R.string.read_full_article))
                        }
                    }
                }
            }
        }
    }
}
