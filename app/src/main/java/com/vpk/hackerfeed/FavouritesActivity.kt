package com.vpk.hackerfeed

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vpk.hackerfeed.components.ArticleCard
import com.vpk.hackerfeed.components.EmptyStateComponent
import com.vpk.hackerfeed.components.ErrorStateComponent
import com.vpk.hackerfeed.components.ExpandedArticleOverlay
import com.vpk.hackerfeed.components.LoadingStateComponent
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.di.ViewModelFactory
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.FavouriteArticle as DomainFavouriteArticle
import com.vpk.hackerfeed.presentation.favourites.FavouritesViewModel
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme

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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favourites_title),
                        style = MaterialTheme.typography.titleLarge
                    )
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
    var expandedArticleId by remember { mutableStateOf<Long?>(null) }
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(favourites) {
        if (expandedArticleId != null && favourites.none { it.id == expandedArticleId }) {
            expandedArticleId = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                items = favourites,
                key = { favourite -> favourite.id }
            ) { favourite ->
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
                    isFavourite = true,
                    onToggleFavourite = { onRemoveFavourite(favourite.id) },
                    onLongClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        expandedArticleId = favourite.id
                    },
                    showFavoriteButton = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        expandedArticleId?.let { id ->
            val favourite = favourites.find { it.id == id }
            if (favourite != null) {
                val article = Article(
                    id = favourite.id,
                    author = favourite.author,
                    score = favourite.score,
                    time = favourite.time,
                    title = favourite.title,
                    url = favourite.url
                )
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = true,
                    onToggleFavourite = { onRemoveFavourite(favourite.id) },
                    onDismiss = { expandedArticleId = null }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    HackerFeedTheme(darkTheme = true) {
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

        Surface(color = MaterialTheme.colorScheme.background) {
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
    HackerFeedTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EmptyStateComponent(
                icon = Icons.Filled.Favorite,
                title = stringResource(R.string.no_favourites),
                description = stringResource(R.string.no_favourites_description)
            )
        }
    }
}
