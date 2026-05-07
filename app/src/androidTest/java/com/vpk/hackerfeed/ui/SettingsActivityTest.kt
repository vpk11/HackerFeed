package com.vpk.hackerfeed.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.SettingsScreen
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class SettingsActivityTest {

    companion object {
        private const val TITLE = "Settings"
        private const val SECTION_APP_SETTINGS = "App Settings"
        private const val SECTION_PRIVACY_LEGAL = "Privacy & Legal"
        private const val SECTION_GENERAL = "General"
        private const val ITEM_CACHE = "Cache Management"
        private const val ITEM_PRIVACY = "Privacy Policy"
        private const val ITEM_TERMS = "Terms & Conditions"
        private const val ITEM_DATA = "Data Protection"
        private const val ITEM_ABOUT = "About"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_displaysTitle() {
        composeTestRule.setContent {
            HackerFeedTheme { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(TITLE).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysAllSections() {
        composeTestRule.setContent {
            HackerFeedTheme { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(SECTION_APP_SETTINGS).assertIsDisplayed()
        composeTestRule.onNodeWithText(SECTION_PRIVACY_LEGAL).assertIsDisplayed()
        composeTestRule.onNodeWithText(SECTION_GENERAL).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysCacheManagement() {
        composeTestRule.setContent {
            HackerFeedTheme { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(ITEM_CACHE).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysPrivacyItems() {
        composeTestRule.setContent {
            HackerFeedTheme { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(ITEM_PRIVACY).assertIsDisplayed()
        composeTestRule.onNodeWithText(ITEM_TERMS).assertIsDisplayed()
        composeTestRule.onNodeWithText(ITEM_DATA).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysAbout() {
        composeTestRule.setContent {
            HackerFeedTheme { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(ITEM_ABOUT).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(TITLE).assertIsDisplayed()
    }

    @Test
    fun settingsScreen_lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) { SettingsScreen() }
        }

        composeTestRule.onNodeWithText(TITLE).assertIsDisplayed()
    }
}
