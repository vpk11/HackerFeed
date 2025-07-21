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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vpk.hackerfeed.di.ViewModelFactory
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.presentation.news.NewsViewModel
import com.vpk.hackerfeed.presentation.news.NewsUiState
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import com.vpk.hackerfeed.components.AnimatedFavoriteButton
import com.vpk.hackerfeed.components.ThemedTopAppBar
import com.vpk.hackerfeed.components.ArticleListComponent
import com.vpk.hackerfeed.components.LoadingStateComponent
import com.vpk.hackerfeed.components.ErrorStateComponent
import com.vpk.hackerfeed.components.EmptyStateComponent
import com.vpk.hackerfeed.components.ArticleCard

class NewsActivity : ComponentActivity() {
    private val viewModel: NewsViewModel by viewModels {
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
                    NewsApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(viewModel: NewsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val localContext = LocalContext.current

    Scaffold(
        topBar = {
            ThemedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(localContext, FavouritesActivity::class.java)
                        localContext.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.favourites_title)
                        )
                    }
                    IconButton(onClick = {
                        val intent = Intent(localContext, SettingsActivity::class.java)
                        localContext.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_content_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshTopStories() }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val error = uiState.error
                if (uiState.isLoading && uiState.storyIds.isEmpty() && !isRefreshing) {
                    LoadingStateComponent()
                } else if (error != null && !isRefreshing) {
                    ErrorStateComponent(errorMessage = error)
                } else if (uiState.storyIds.isNotEmpty()) {
                    ArticleListComponent(
                        storyIds = uiState.storyIds,
                        articles = uiState.articles,
                        favouriteArticleIds = uiState.favouriteArticleIds,
                        onFetchArticle = { id -> viewModel.fetchArticleDetails(id) },
                        onToggleFavourite = { article -> viewModel.toggleFavourite(article) }
                    )
                } else if (!uiState.isLoading && !isRefreshing) {
                    EmptyStateComponent(
                        icon = Icons.Filled.Info,
                        title = stringResource(R.string.no_articles_available),
                        description = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewsAppScrollablePreview() {
    HackerFeedTheme {
        val previewUiState = NewsUiState(
            storyIds = listOf(1, 2, 3),
            articles = mapOf<Long, Article>(
                1L to Article(1, "Author One", 100, 0, "First Long Article Title That Might Wrap Around Nicely", "http://example.com/1"),
                2L to Article(2, "Author Two", 120, 0, "Second Article Also Interesting", "http://example.com/2")
            ),
            isLoading = false
        )
        Scaffold(
            topBar = {
                ThemedTopAppBar(
                    title = { Text("HackerFeed Scrollable") }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                ArticleListComponent(
                    storyIds = previewUiState.storyIds,
                    articles = previewUiState.articles,
                    favouriteArticleIds = setOf(1L), // Preview with one favourite
                    onFetchArticle = {},
                    onToggleFavourite = {}
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ArticleCardInListPreview() {
    val previewArticle = Article(
        id = 1,
        author = "dev.to",
        score = 256,
        time = 0,
        title = "Top 10 Kotlin Coroutine Scopes You Must Know in 2024 for Better App Performance",
        url = "https://dev.to/kotlin/coroutines"
    )
    HackerFeedTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ArticleCard(
                article = previewArticle,
                isFavourite = true,
                onToggleFavourite = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, name = "Animated Favorite Button")
@Composable
fun AnimatedFavoriteButtonPreview() {
    HackerFeedTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Animated Favorite Button Demo",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedFavoriteButton(
                            isFavourite = false,
                            onClick = { }
                        )
                        Text(
                            text = "Add to Favourites",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedFavoriteButton(
                            isFavourite = true,
                            onClick = { }
                        )
                        Text(
                            text = "Remove from Favourites",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                Text(
                    text = "Tap the buttons to see the bounce animation!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
