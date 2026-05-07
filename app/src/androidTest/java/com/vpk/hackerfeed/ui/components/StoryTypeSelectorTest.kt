package com.vpk.hackerfeed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vpk.hackerfeed.StoryTypeSelector
import com.vpk.hackerfeed.domain.model.StoryType
import com.vpk.hackerfeed.ui.theme.HackerFeedTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StoryTypeSelectorTest {

    companion object {
        private const val LABEL_TOP = "Top"
        private const val LABEL_NEW = "New"
        private const val LABEL_BEST = "Best"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysAllThreeChips() {
        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(selected = StoryType.TOP, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_TOP).assertIsDisplayed()
        composeTestRule.onNodeWithText(LABEL_NEW).assertIsDisplayed()
        composeTestRule.onNodeWithText(LABEL_BEST).assertIsDisplayed()
    }

    @Test
    fun topSelected_topChipIsSelected() {
        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(selected = StoryType.TOP, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_TOP).assertIsSelected()
        composeTestRule.onNodeWithText(LABEL_NEW).assertIsNotSelected()
        composeTestRule.onNodeWithText(LABEL_BEST).assertIsNotSelected()
    }

    @Test
    fun newSelected_newChipIsSelected() {
        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(selected = StoryType.NEW, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_NEW).assertIsSelected()
    }

    @Test
    fun bestSelected_bestChipIsSelected() {
        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(selected = StoryType.BEST, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_BEST).assertIsSelected()
    }

    @Test
    fun clickNewChip_invokesCallbackWithNew() {
        var selectedType: StoryType? = null

        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(
                    selected = StoryType.TOP,
                    onSelect = { selectedType = it }
                )
            }
        }

        composeTestRule.onNodeWithText(LABEL_NEW).performClick()

        assertEquals(StoryType.NEW, selectedType)
    }

    @Test
    fun clickBestChip_invokesCallbackWithBest() {
        var selectedType: StoryType? = null

        composeTestRule.setContent {
            HackerFeedTheme {
                StoryTypeSelector(
                    selected = StoryType.TOP,
                    onSelect = { selectedType = it }
                )
            }
        }

        composeTestRule.onNodeWithText(LABEL_BEST).performClick()

        assertEquals(StoryType.BEST, selectedType)
    }

    @Test
    fun darkTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = true) {
                StoryTypeSelector(selected = StoryType.TOP, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_TOP).assertIsDisplayed()
    }

    @Test
    fun lightTheme_renders() {
        composeTestRule.setContent {
            HackerFeedTheme(darkTheme = false) {
                StoryTypeSelector(selected = StoryType.NEW, onSelect = {})
            }
        }

        composeTestRule.onNodeWithText(LABEL_NEW).assertIsDisplayed()
    }
}
