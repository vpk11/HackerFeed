package com.vpk.hackerfeed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.components.ExpandedArticleOverlay
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class ExpandedArticleOverlayTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overlay_displaysFullTitle() {
        val article = TestData.article(title = TestData.Titles.LONG)

        composeTestRule.setContent {
            HackerFeedTheme {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.LONG).assertIsDisplayed()
    }

    @Test
    fun overlay_displaysAuthor() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("by ${article.author}", substring = true).assertIsDisplayed()
    }

    @Test
    fun overlay_displaysScore() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("${article.score} points", substring = true).assertIsDisplayed()
    }

    @Test
    fun overlay_displaysReadButton() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Read Full Article").assertIsDisplayed()
    }

    @Test
    fun overlay_noUrl_hidesReadButton() {
        val article = TestData.article(url = null)

        composeTestRule.setContent {
            HackerFeedTheme {
                ExpandedArticleOverlay(
                    article = article,
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Read Full Article").assertDoesNotExist()
    }

    @Test
    fun overlay_darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                ExpandedArticleOverlay(
                    article = TestData.article(),
                    isFavourite = true,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }

    @Test
    fun overlay_lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                ExpandedArticleOverlay(
                    article = TestData.article(),
                    isFavourite = false,
                    onToggleFavourite = {},
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }
}
