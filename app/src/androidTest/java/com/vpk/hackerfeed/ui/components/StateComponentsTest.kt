package com.vpk.hackerfeed.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.components.EmptyStateComponent
import com.vpk.hackerfeed.components.ErrorStateComponent
import com.vpk.hackerfeed.components.LoadingStateComponent
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class StateComponentsTest {

    companion object {
        private const val LOADING_MESSAGE = "Loading data..."
        private const val EMPTY_TITLE = "Nothing here"
        private const val EMPTY_DESCRIPTION = "Try adding some items"
        private const val ERROR_MESSAGE = "Something went wrong"
        private const val SIGNAL_LOST = "[ SIGNAL LOST ]"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysMessage() {
        composeTestRule.setContent {
            HackerFeedTheme { LoadingStateComponent(message = LOADING_MESSAGE) }
        }

        composeTestRule.onNodeWithText(LOADING_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun loadingState_noMessage_rendersWithoutCrash() {
        composeTestRule.setContent {
            HackerFeedTheme { LoadingStateComponent() }
        }

        composeTestRule.onNodeWithText(LOADING_MESSAGE).assertDoesNotExist()
    }

    @Test
    fun emptyState_displaysTitle() {
        composeTestRule.setContent {
            HackerFeedTheme {
                EmptyStateComponent(
                    icon = Icons.Filled.Favorite,
                    title = EMPTY_TITLE
                )
            }
        }

        composeTestRule.onNodeWithText(EMPTY_TITLE).assertIsDisplayed()
    }

    @Test
    fun emptyState_displaysDescription() {
        composeTestRule.setContent {
            HackerFeedTheme {
                EmptyStateComponent(
                    icon = Icons.Filled.Favorite,
                    title = EMPTY_TITLE,
                    description = EMPTY_DESCRIPTION
                )
            }
        }

        composeTestRule.onNodeWithText(EMPTY_DESCRIPTION).assertIsDisplayed()
    }

    @Test
    fun emptyState_noDescription_rendersWithoutCrash() {
        composeTestRule.setContent {
            HackerFeedTheme {
                EmptyStateComponent(
                    icon = Icons.Filled.Favorite,
                    title = EMPTY_TITLE
                )
            }
        }

        composeTestRule.onNodeWithText(EMPTY_DESCRIPTION).assertDoesNotExist()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        composeTestRule.setContent {
            HackerFeedTheme { ErrorStateComponent(errorMessage = ERROR_MESSAGE) }
        }

        composeTestRule.onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun errorState_displaysSignalLost() {
        composeTestRule.setContent {
            HackerFeedTheme { ErrorStateComponent(errorMessage = ERROR_MESSAGE) }
        }

        composeTestRule.onNodeWithText(SIGNAL_LOST).assertIsDisplayed()
    }

    @Test
    fun darkTheme_allStatesRender() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                ErrorStateComponent(errorMessage = ERROR_MESSAGE)
            }
        }

        composeTestRule.onNodeWithText(SIGNAL_LOST).assertIsDisplayed()
    }

    @Test
    fun lightTheme_allStatesRender() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                ErrorStateComponent(errorMessage = ERROR_MESSAGE)
            }
        }

        composeTestRule.onNodeWithText(SIGNAL_LOST).assertIsDisplayed()
    }
}
