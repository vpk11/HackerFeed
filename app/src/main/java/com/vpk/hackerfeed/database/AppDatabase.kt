package com.vpk.hackerfeed.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [
        FavouriteArticle::class,
        CachedArticle::class,
        CachedTopStories::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteArticleDao(): FavouriteArticleDao
    abstract fun cachedArticleDao(): CachedArticleDao
    abstract fun cachedTopStoriesDao(): CachedTopStoriesDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create cached_articles table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS cached_articles (
                        id INTEGER NOT NULL PRIMARY KEY,
                        author TEXT,
                        score INTEGER,
                        time INTEGER,
                        title TEXT,
                        url TEXT,
                        cachedAt INTEGER NOT NULL
                    )
                """)
                
                // Create cached_top_stories table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS cached_top_stories (
                        id INTEGER NOT NULL PRIMARY KEY,
                        storyIds TEXT NOT NULL,
                        cachedAt INTEGER NOT NULL
                    )
                """)
            }
        }
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hackerfeed_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
