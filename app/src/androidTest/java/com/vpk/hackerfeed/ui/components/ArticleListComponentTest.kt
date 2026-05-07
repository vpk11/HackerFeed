package com.vpk.hackerfeed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.components.ArticleListComponent
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class ArticleListComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createArticlesMap(): Map<Long, Article?> = mapOf(
        TestData.Ids.ARTICLE_1 to TestData.article(
            id = TestData.Ids.ARTICLE_1,
            title = "First Article"
        ),
        TestData.Ids.ARTICLE_2 to TestData.article(
            id = TestData.Ids.ARTICLE_2,
            title = "Second Article"
        )
    )

    @Test
    fun displaysList_withLoadedArticles() {
        val storyIds = listOf(TestData.Ids.ARTICLE_1, TestData.Ids.ARTICLE_2)
        val articles = createArticlesMap()

        composeTestRule.setContent {
            HackerFeedTheme {
                ArticleListComponent(
                    storyIds = storyIds,
                    articles = articles,
                    favouriteArticleIds = emptySet(),
                    onFetchArticle = {},
                    onToggleFavourite = {}
                )
            }
        }

        composeTestRule.onNodeWithText("First Article").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Article").assertIsDisplayed()
    }

    @Test
    fun emptyStoryIds_rendersEmptyList() {
        composeTestRule.setContent {
            HackerFeedTheme {
                ArticleListComponent(
                    storyIds = emptyList(),
                    articles = emptyMap(),
                    favouriteArticleIds = emptySet(),
                    onFetchArticle = {},
                    onToggleFavourite = {}
                )
            }
        }

        composeTestRule.onNodeWithText("First Article").assertDoesNotExist()
    }

    @Test
    fun darkTheme_renders() {
        val storyIds = listOf(TestData.Ids.ARTICLE_1)
        val articles = mapOf(
            TestData.Ids.ARTICLE_1 to TestData.article(id = TestData.Ids.ARTICLE_1)
        )

        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                ArticleListComponent(
                    storyIds = storyIds,
                    articles = articles,
                    favouriteArticleIds = emptySet(),
                    onFetchArticle = {},
                    onToggleFavourite = {}
                )
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }
}
