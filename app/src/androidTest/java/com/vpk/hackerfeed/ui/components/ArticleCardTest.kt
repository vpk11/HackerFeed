package com.vpk.hackerfeed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.components.ArticleCard
import com.vpk.hackerfeed.helpers.TestData
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class ArticleCardTest {

    companion object {
        private const val READ_BUTTON = "Read Full Article"
        private const val ADD_FAVOURITE = "Add to favourites"
        private const val REMOVE_FAVOURITE = "Remove from favourites"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadedCard_displaysTitle() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText(article.title!!).assertIsDisplayed()
    }

    @Test
    fun loadedCard_displaysAuthor() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText("by ${article.author}", substring = true).assertIsDisplayed()
    }

    @Test
    fun loadedCard_displaysScore() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText("${article.score} points", substring = true).assertIsDisplayed()
    }

    @Test
    fun loadedCard_displaysReadButton() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText(READ_BUTTON).assertIsDisplayed()
    }

    @Test
    fun loadedCard_withNoUrl_hidesReadButton() {
        val article = TestData.article(url = null)

        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = article) }
        }

        composeTestRule.onNodeWithText(READ_BUTTON).assertDoesNotExist()
    }

    @Test
    fun nullArticle_showsLoadingIndicator() {
        composeTestRule.setContent {
            HackerFeedTheme { ArticleCard(article = null) }
        }

        composeTestRule.onNodeWithText(READ_BUTTON).assertDoesNotExist()
    }

    @Test
    fun favouriteButton_shownByDefault() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme {
                ArticleCard(article = article, showFavoriteButton = true)
            }
        }

        composeTestRule.onNode(hasContentDescription(ADD_FAVOURITE)).assertIsDisplayed()
    }

    @Test
    fun favouriteButton_hiddenWhenDisabled() {
        val article = TestData.article()

        composeTestRule.setContent {
            HackerFeedTheme {
                ArticleCard(article = article, showFavoriteButton = false)
            }
        }

        composeTestRule.onNode(hasContentDescription(ADD_FAVOURITE)).assertDoesNotExist()
        composeTestRule.onNode(hasContentDescription(REMOVE_FAVOURITE)).assertDoesNotExist()
    }

    @Test
    fun darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                ArticleCard(article = TestData.article())
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }

    @Test
    fun lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                ArticleCard(article = TestData.article())
            }
        }

        composeTestRule.onNodeWithText(TestData.Titles.PRIMARY).assertIsDisplayed()
    }
}
