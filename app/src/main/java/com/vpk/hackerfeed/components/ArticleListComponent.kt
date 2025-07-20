package com.vpk.hackerfeed.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpk.hackerfeed.domain.model.Article

/**
 * A reusable lazy column component for displaying a list of articles.
 * Handles automatic fetching of article details when needed.
 *
 * @param storyIds List of story IDs to display
 * @param articles Map of article ID to Article (null if not yet loaded)
 * @param favouriteArticleIds Set of favorited article IDs
 * @param onFetchArticle Callback to fetch article details for a given ID
 * @param onToggleFavourite Callback when favorite button is clicked
 * @param showFavoriteButton Whether to show favorite buttons on cards
 * @param modifier Modifier for the lazy column
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
        }
    }
}
