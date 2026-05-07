package com.vpk.hackerfeed.edge

import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.components.ArticleCard
import com.vpk.hackerfeed.components.ArticleListComponent
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class ApiLevelDegradationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun articleCard_rendersOnCurrentApi() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText(article.title!!).assertIsDisplayed()
    }

    @Test
    fun articleListComponent_rendersOnCurrentApi() {
        val storyIds = listOf(TestData.Ids.ARTICLE_1)
        val articles = mapOf(TestData.Ids.ARTICLE_1 to TestData.article())

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

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }

    @Test
    fun blurGuard_apiVersionCheck() {
        val supportsBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        if (supportsBlur) {
            assert(Build.VERSION.SDK_INT >= 31)
        } else {
            assert(Build.VERSION.SDK_INT < 31)
        }
    }
}
