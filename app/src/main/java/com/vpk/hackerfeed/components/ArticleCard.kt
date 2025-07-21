package com.vpk.hackerfeed.components

import android.content.Intent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme

/**
 * A reusable article card component that displays article information
 * with consistent styling across the app.
 *
 * @param article The article to display, null shows loading state
 * @param isFavourite Whether the article is favorited
 * @param onToggleFavourite Callback for favorite button click
 * @param showFavoriteButton Whether to show the favorite button
 * @param modifier Modifier for the card
 */
@Composable
fun ArticleCard(
    article: Article?,
    isFavourite: Boolean = false,
    onToggleFavourite: () -> Unit = {},
    showFavoriteButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cardContentColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
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
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
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
                                style = MaterialTheme.typography.titleLarge,
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
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
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
