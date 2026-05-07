package com.vpk.hackerfeed.components

import android.content.Intent
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.ui.theme.ElectricCyan
import com.vpk.hackerfeed.ui.theme.JetBrainsMono
import com.vpk.hackerfeed.ui.theme.ElectricViolet
import com.vpk.hackerfeed.ui.theme.ElectricVioletLight
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExpandedArticleOverlay(
    article: Article,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val buttonColor = if (isSystemInDarkTheme()) ElectricViolet else ElectricVioletLight

    val scaleAnim = remember { Animatable(0.8f) }
    val scrimAlphaAnim = remember { Animatable(0f) }
    var isDismissing by remember { mutableStateOf(false) }

    val dismissOverlay: () -> Unit = remember {
        { isDismissing = true }
    }

    BackHandler(onBack = dismissOverlay)

    LaunchedEffect(Unit) {
        launch {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            scrimAlphaAnim.animateTo(1f, tween(250))
        }
    }

    LaunchedEffect(isDismissing) {
        if (isDismissing) {
            launch { scaleAnim.animateTo(0.85f, tween(200)) }
            launch { scrimAlphaAnim.animateTo(0f, tween(200)) }
            delay(220)
            onDismiss()
        }
    }

    val normalizedProgress = ((scaleAnim.value - 0.8f) / 0.2f).coerceIn(0f, 1.5f)
    val rotationX = (1f - normalizedProgress.coerceAtMost(1f)) * 5f
    val cardElevation = 4f + (12f * normalizedProgress.coerceAtMost(1f))

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { dismissOverlay() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlphaAnim.value * 0.7f))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                    this.rotationX = rotationX
                    shadowElevation = cardElevation
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.7f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = article.title ?: stringResource(R.string.no_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold
                    )
                )

                val authorName = article.author ?: stringResource(R.string.unknown)
                Text(
                    text = stringResource(R.string.article_author, authorName),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.points, article.score ?: 0),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (article.time != null && article.time > 0) {
                        Text(
                            text = formatRelativeTimestamp(article.time),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    AnimatedFavoriteButton(
                        isFavourite = isFavourite,
                        onClick = onToggleFavourite
                    )
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

private fun formatRelativeTimestamp(epochSeconds: Long): String {
    val now = System.currentTimeMillis()
    val timeMillis = epochSeconds * 1000
    return DateUtils.getRelativeTimeSpanString(
        timeMillis,
        now,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}

@Preview(showBackground = true, name = "Expanded Overlay – Dark")
@Composable
private fun ExpandedArticleOverlayDarkPreview() {
    HackerFeedTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpandedArticleOverlay(
                article = Article(
                    id = 1,
                    title = "Show HN: A Very Long Title That Would Normally Be Clamped to Two Lines in the Regular Card View but Is Fully Visible Here",
                    author = "hacker",
                    score = 312,
                    time = System.currentTimeMillis() / 1000 - 7200,
                    url = "https://example.com"
                ),
                isFavourite = true,
                onToggleFavourite = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Expanded Overlay – Light")
@Composable
private fun ExpandedArticleOverlayLightPreview() {
    HackerFeedTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpandedArticleOverlay(
                article = Article(
                    id = 2,
                    title = "Ask HN: What Are Your Favorite Underrated Developer Tools That Deserve More Attention from the Community?",
                    author = "dev_curious",
                    score = 89,
                    time = System.currentTimeMillis() / 1000 - 3600,
                    url = "https://example.com"
                ),
                isFavourite = false,
                onToggleFavourite = {},
                onDismiss = {}
            )
        }
    }
}
