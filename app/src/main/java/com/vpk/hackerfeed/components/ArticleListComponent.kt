package com.vpk.hackerfeed.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.ui.theme.ElectricCyan

/**
 * Cyberpunk-styled lazy column for displaying article cards
 * with neon dividers and increased spacing.
 */
@Composable
fun ArticleListComponent(
    storyIds: List<Long>,
    articles: Map<Long, Article?>,
    favouriteArticleIds: Set<Long>,
    onFetchArticle: (Long) -> Unit,
    onToggleFavourite: (Article) -> Unit,
    showFavoriteButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
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
                showFavoriteButton = showFavoriteButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Subtle neon divider between cards
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp),
                thickness = 0.5.dp,
                color = ElectricCyan.copy(alpha = 0.15f)
            )
        }
    }
}
