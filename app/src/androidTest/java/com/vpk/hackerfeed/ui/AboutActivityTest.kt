package com.vpk.hackerfeed.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.AboutScreenContent
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class AboutActivityTest {

    companion object {
        private const val APP_NAME = "HackerFeed"
        private const val DEVELOPED_BY = "Developed by Vishnuprasad Kamalon"
        private const val CONNECT_TEXT = "Connect with developer:"
        private const val PORTFOLIO = "Portfolio"
        private const val EMAIL = "Email"
        private const val GITHUB = "Github"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun aboutScreen_displaysAppName() {
        composeTestRule.setContent {
            HackerFeedTheme { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(APP_NAME, substring = true).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_displaysDevelopedBy() {
        composeTestRule.setContent {
            HackerFeedTheme { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(DEVELOPED_BY).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_displaysVersionNumber() {
        composeTestRule.setContent {
            HackerFeedTheme { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText("Version", substring = true).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_displaysSocialLinks() {
        composeTestRule.setContent {
            HackerFeedTheme { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(PORTFOLIO).assertIsDisplayed()
        composeTestRule.onNodeWithText(EMAIL).assertIsDisplayed()
        composeTestRule.onNodeWithText(GITHUB).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_displaysConnectText() {
        composeTestRule.setContent {
            HackerFeedTheme { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(CONNECT_TEXT).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(APP_NAME, substring = true).assertIsDisplayed()
    }

    @Test
    fun aboutScreen_lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) { AboutScreenContent() }
        }

        composeTestRule.onNodeWithText(APP_NAME, substring = true).assertIsDisplayed()
    }
}
