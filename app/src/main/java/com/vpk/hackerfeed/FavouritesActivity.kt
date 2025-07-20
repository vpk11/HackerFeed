package com.vpk.hackerfeed

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vpk.hackerfeed.database.FavouriteArticle
import com.vpk.hackerfeed.di.ViewModelFactory
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.FavouriteArticle as DomainFavouriteArticle
import com.vpk.hackerfeed.presentation.favourites.FavouritesViewModel
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import com.vpk.hackerfeed.ui.theme.getCardBackgroundColor
import com.vpk.hackerfeed.components.AnimatedFavoriteButton
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.components.LoadingStateComponent
import com.vpk.hackerfeed.components.EmptyStateComponent
import com.vpk.hackerfeed.components.ErrorStateComponent
import com.vpk.hackerfeed.components.ArticleCard

class FavouritesActivity : ComponentActivity() {
    private val viewModel: FavouritesViewModel by viewModels {
        ViewModelFactory((application as HackerFeedApplication).container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HackerFeedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FavouritesScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(viewModel: FavouritesViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localContext = LocalContext.current

    Scaffold(
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.favourites_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (localContext is ComponentActivity) {
                            localContext.finish()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back_content_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val error = uiState.error
            if (uiState.isLoading) {
                LoadingStateComponent()
            } else if (error != null) {
                ErrorStateComponent(errorMessage = error)
            } else if (uiState.favouriteArticles.isEmpty()) {
                EmptyStateComponent(
                    icon = Icons.Filled.Favorite,
                    title = stringResource(R.string.no_favourites),
                    description = stringResource(R.string.no_favourites_description)
                )
            } else {
                FavouritesList(
                    favourites = uiState.favouriteArticles,
                    onRemoveFavourite = { articleId -> viewModel.removeFromFavourites(articleId) }
                )
            }
        }
    }
}

@Composable
fun FavouritesList(
    favourites: List<DomainFavouriteArticle>,
    onRemoveFavourite: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = favourites,
            key = { favourite -> favourite.id }
        ) { favourite ->
            // Convert FavouriteArticle to Article for reusable component
            val article = Article(
                id = favourite.id,
                author = favourite.author,
                score = favourite.score,
                time = favourite.time,
                title = favourite.title,
                url = favourite.url
            )
            
            ArticleCard(
                article = article,
                isFavourite = true, // Always true in favourites screen
                onToggleFavourite = { onRemoveFavourite(favourite.id) },
                showFavoriteButton = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    HackerFeedTheme {
        val previewFavourites = listOf(
            DomainFavouriteArticle(
                id = 1,
                title = "Amazing Kotlin Features You Should Know",
                author = "dev.kotlin",
                score = 256,
                time = 0,
                url = "https://example.com/1",
                dateAdded = System.currentTimeMillis()
            ),
            DomainFavouriteArticle(
                id = 2,
                title = "Building Android Apps with Jetpack Compose",
                author = "android.dev",
                score = 189,
                time = 0,
                url = "https://example.com/2",
                dateAdded = System.currentTimeMillis()
            )
        )
        
        Surface {
            FavouritesList(
                favourites = previewFavourites,
                onRemoveFavourite = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Empty Favourites State")
@Composable
fun EmptyFavouritesStatePreview() {
    HackerFeedTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.no_favourites),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.no_favourites_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
