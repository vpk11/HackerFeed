package com.vpk.hackerfeed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.vpk.hackerfeed.components.AnimatedFavoriteButton
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AnimatedFavoriteButtonTest {

    companion object {
        private const val ADD_DESCRIPTION = "Add to favourites"
        private const val REMOVE_DESCRIPTION = "Remove from favourites"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun unfavourited_showsAddDescription() {
        composeTestRule.setContent {
            HackerFeedTheme {
                AnimatedFavoriteButton(isFavourite = false, onClick = {})
            }
        }

        composeTestRule.onNode(hasContentDescription(ADD_DESCRIPTION)).assertIsDisplayed()
    }

    @Test
    fun favourited_showsRemoveDescription() {
        composeTestRule.setContent {
            HackerFeedTheme {
                AnimatedFavoriteButton(isFavourite = true, onClick = {})
            }
        }

        composeTestRule.onNode(hasContentDescription(REMOVE_DESCRIPTION)).assertIsDisplayed()
    }

    @Test
    fun click_invokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            HackerFeedTheme {
                AnimatedFavoriteButton(isFavourite = false, onClick = { clicked = true })
            }
        }

        composeTestRule.onNode(hasContentDescription(ADD_DESCRIPTION)).performClick()

        assertTrue(clicked)
    }

    @Test
    fun darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                AnimatedFavoriteButton(isFavourite = true, onClick = {})
            }
        }

        composeTestRule.onNode(hasContentDescription(REMOVE_DESCRIPTION)).assertIsDisplayed()
    }

    @Test
    fun lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                AnimatedFavoriteButton(isFavourite = false, onClick = {})
            }
        }

        composeTestRule.onNode(hasContentDescription(ADD_DESCRIPTION)).assertIsDisplayed()
    }
}
