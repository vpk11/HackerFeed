package com.vpk.hackerfeed.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MigrationTest {

    companion object {
        private const val DB_NAME = "migration-test-db"
        private const val CACHED_ARTICLES_TABLE = "cached_articles"
        private const val CACHED_TOP_STORIES_TABLE = "cached_top_stories"
    }

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun migrate1To2_createsCachedArticlesTable() {
        helper.createDatabase(DB_NAME, 1)

        val db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM $CACHED_ARTICLES_TABLE").use { cursor ->
            assertEquals(0, cursor.count)

            val expectedColumns = listOf("id", "author", "score", "time", "title", "url", "cachedAt")
            val actualColumns = (0 until cursor.columnCount).map { cursor.getColumnName(it) }
            expectedColumns.forEach { column ->
                assert(actualColumns.contains(column)) {
                    "Missing column '$column' in $CACHED_ARTICLES_TABLE"
                }
            }
        }
    }

    @Test
    fun migrate1To2_createsCachedTopStoriesTable() {
        helper.createDatabase(DB_NAME, 1)

        val db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM $CACHED_TOP_STORIES_TABLE").use { cursor ->
            assertEquals(0, cursor.count)

            val expectedColumns = listOf("id", "storyIds", "cachedAt")
            val actualColumns = (0 until cursor.columnCount).map { cursor.getColumnName(it) }
            expectedColumns.forEach { column ->
                assert(actualColumns.contains(column)) {
                    "Missing column '$column' in $CACHED_TOP_STORIES_TABLE"
                }
            }
        }
    }

    @Test
    fun migrate1To2_preservesExistingFavourites() {
        val dbV1 = helper.createDatabase(DB_NAME, 1)
        dbV1.execSQL(
            "INSERT INTO favourite_articles (id, title, author, score, time, url, dateAdded) VALUES (1, 'Title', 'Author', 100, 1700000000, 'https://example.com', 1700000000000)"
        )
        dbV1.close()

        val dbV2 = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        dbV2.query("SELECT * FROM favourite_articles").use { cursor ->
            assertEquals(1, cursor.count)
            cursor.moveToFirst()
            val titleIdx = cursor.getColumnIndex("title")
            assertEquals("Title", cursor.getString(titleIdx))
        }
    }
}
