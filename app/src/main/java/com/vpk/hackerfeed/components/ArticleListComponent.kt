package com.vpk.hackerfeed.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.ui.theme.ElectricCyan

@Composable
fun ArticleListComponent(
    storyIds: List<Long>,
    articles: Map<Long, Article?>,
    favouriteArticleIds: Set<Long>,
    onFetchArticle: (Long) -> Unit,
    onToggleFavourite: (Article) -> Unit,
    modifier: Modifier = Modifier,
    showFavoriteButton: Boolean = true
) {
    var expandedArticleId by remember { mutableStateOf<Long?>(null) }
    val hapticFeedback = LocalHapticFeedback.current

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (expandedArticleId != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.graphicsLayer {
                            renderEffect = android.graphics.RenderEffect
                                .createBlurEffect(
                                    25f, 25f,
                                    android.graphics.Shader.TileMode.CLAMP
                                )
                                .asComposeRenderEffect()
                        }
                    } else {
                        Modifier
                    }
                ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = storyIds,
                key = { storyId -> storyId }
            ) { articleId ->
                LaunchedEffect(articleId, articles[articleId] == null) {
                    if (articles[articleId] == null) {
                        onFetchArticle(articleId)
                    }
                }

                val article = articles[articleId]

                ArticleCard(
                    article = article,
                    isFavourite = favouriteArticleIds.contains(articleId),
                    onToggleFavourite = {
                        article?.let { onToggleFavourite(it) }
                    },
                    onLongClick = {
                        if (article != null) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            expandedArticleId = articleId
                        }
                    },
                    showFavoriteButton = showFavoriteButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp),
                    thickness = 0.5.dp,
                    color = ElectricCyan.copy(alpha = 0.15f)
                )
            }
        }

        expandedArticleId?.let { id ->
            val article = articles[id]
            if (article != null) {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = favouriteArticleIds.contains(id),
                    onToggleFavourite = { onToggleFavourite(article) },
                    onDismiss = { expandedArticleId = null }
                )
            }
        }
    }
}
