package com.vpk.hackerfeed.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vpk.hackerfeed.LegalInfoScreen
import com.vpk.hackerfeed.R
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Rule
import org.junit.Test

class LegalInfoActivityTest {

    companion object {
        private const val PRIVACY_TITLE = "Privacy Policy"
        private const val TERMS_TITLE = "Terms & Conditions"
        private const val DATA_TITLE = "Data Protection"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun privacyPolicy_displaysTitle() {
        composeTestRule.setContent {
            HackerFeedTheme {
                LegalInfoScreen(
                    titleResId = R.string.privacy_policy_title,
                    contentResId = R.string.privacy_policy_content
                )
            }
        }

        composeTestRule.onNodeWithText(PRIVACY_TITLE).assertIsDisplayed()
    }

    @Test
    fun termsConditions_displaysTitle() {
        composeTestRule.setContent {
            HackerFeedTheme {
                LegalInfoScreen(
                    titleResId = R.string.terms_conditions_title,
                    contentResId = R.string.terms_conditions_content
                )
            }
        }

        composeTestRule.onNodeWithText(TERMS_TITLE).assertIsDisplayed()
    }

    @Test
    fun dataProtection_displaysTitle() {
        composeTestRule.setContent {
            HackerFeedTheme {
                LegalInfoScreen(
                    titleResId = R.string.data_protection_title,
                    contentResId = R.string.data_protection_content
                )
            }
        }

        composeTestRule.onNodeWithText(DATA_TITLE).assertIsDisplayed()
    }

    @Test
    fun privacyPolicy_displaysContent() {
        composeTestRule.setContent {
            HackerFeedTheme {
                LegalInfoScreen(
                    titleResId = R.string.privacy_policy_title,
                    contentResId = R.string.privacy_policy_content
                )
            }
        }

        composeTestRule.onNodeWithText("Data We Collect", substring = true).assertIsDisplayed()
    }

    @Test
    fun darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                LegalInfoScreen(
                    titleResId = R.string.privacy_policy_title,
                    contentResId = R.string.privacy_policy_content
                )
            }
        }

        composeTestRule.onNodeWithText(PRIVACY_TITLE).assertIsDisplayed()
    }

    @Test
    fun lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                LegalInfoScreen(
                    titleResId = R.string.data_protection_title,
                    contentResId = R.string.data_protection_content
                )
            }
        }

        composeTestRule.onNodeWithText(DATA_TITLE).assertIsDisplayed()
    }
}
