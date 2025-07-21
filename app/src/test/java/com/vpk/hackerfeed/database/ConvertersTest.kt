package com.vpk.hackerfeed.database

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for the Converters class.
 */
class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `fromList converts List to JSON string correctly`() {
        // Arrange
        val storyIds = listOf(1L, 2L, 3L, 4L, 5L)
        
        // Act
        val result = converters.fromList(storyIds)
        
        // Assert
        assertNotNull(result)
        assertTrue(result!!.contains("1"))
        assertTrue(result.contains("2"))
        assertTrue(result.contains("3"))
        assertTrue(result.contains("4"))
        assertTrue(result.contains("5"))
    }

    @Test
    fun `fromString converts JSON string to List correctly`() {
        // Arrange
        val jsonString = "[1,2,3,4,5]"
        val expectedList = listOf(1L, 2L, 3L, 4L, 5L)
        
        // Act
        val result = converters.fromString(jsonString)
        
        // Assert
        assertNotNull(result)
        assertEquals(expectedList, result)
    }

    @Test
    fun `fromList handles null input`() {
        // Act
        val result = converters.fromList(null)
        
        // Assert
        assertNull(result)
    }

    @Test
    fun `fromString handles null input`() {
        // Act
        val result = converters.fromString(null)
        
        // Assert
        assertNull(result)
    }

    @Test
    fun `roundtrip conversion preserves data`() {
        // Arrange
        val originalList = listOf(123L, 456L, 789L, 1000L, 2000L)
        
        // Act
        val jsonString = converters.fromList(originalList)
        val convertedList = converters.fromString(jsonString)
        
        // Assert
        assertEquals(originalList, convertedList)
    }

    @Test
    fun `fromString handles empty list JSON`() {
        // Arrange
        val emptyListJson = "[]"
        
        // Act
        val result = converters.fromString(emptyListJson)
        
        // Assert
        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun `fromList handles empty list`() {
        // Arrange
        val emptyList = emptyList<Long>()
        
        // Act
        val result = converters.fromList(emptyList)
        
        // Assert
        assertNotNull(result)
        assertEquals("[]", result)
    }
}
