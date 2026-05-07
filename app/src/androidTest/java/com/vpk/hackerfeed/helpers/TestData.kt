package com.vpk.hackerfeed.helpers

import com.vpk.hackerfeed.database.CachedArticle
import com.vpk.hackerfeed.database.CachedTopStories
import com.vpk.hackerfeed.domain.model.Article
import com.vpk.hackerfeed.domain.model.FavouriteArticle
import com.vpk.hackerfeed.database.FavouriteArticle as DbFavouriteArticle

object TestData {

    object Ids {
        const val ARTICLE_1 = 100L
        const val ARTICLE_2 = 200L
        const val ARTICLE_3 = 300L
        const val ARTICLE_4 = 400L
        const val ARTICLE_5 = 500L
        val TOP_STORIES = listOf(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4, ARTICLE_5)
    }

    object Authors {
        const val PRIMARY = "testauthor"
        const val SECONDARY = "another_author"
        const val FAVOURITE = "favauthor"
    }

    object Scores {
        const val HIGH = 256
        const val MEDIUM = 100
        const val LOW = 50
    }

    object Timestamps {
        const val EPOCH_RECENT = 1_700_000_000L
        const val EPOCH_OLD = 1_600_000_000L
        val NOW get() = System.currentTimeMillis()
        fun minutesAgo(minutes: Int) = NOW - minutes * 60 * 1_000L
        fun hoursAgo(hours: Int) = NOW - hours * 60 * 60 * 1_000L
    }

    object Urls {
        const val ARTICLE = "https://example.com/article"
        const val FAVOURITE = "https://example.com/fav"
        const val CACHED = "https://example.com/cached"
    }

    object Titles {
        const val PRIMARY = "Test Article Title"
        const val MINIMAL = "Minimal Article"
        const val FAVOURITE = "Favourite Article"
        const val CACHED = "Cached Article"
        const val LONG = "This Is a Very Long Article Title That Would Be Clamped to Two Lines in the Card View"
    }

    object Fixtures {
        const val TOP_STORIES = "top_stories.json"
        const val ARTICLE_DETAIL = "article_detail.json"
        const val ARTICLE_DETAIL_MINIMAL = "article_detail_minimal.json"
        const val ERROR_404 = "error_404.json"
        const val ERROR_500 = "error_500.json"
        const val MALFORMED = "malformed.json"
    }

    object Http {
        const val STATUS_OK = 200
        const val STATUS_NOT_FOUND = 404
        const val STATUS_SERVER_ERROR = 500
        const val CONTENT_TYPE_JSON = "application/json"
    }

    fun article(
        id: Long = Ids.ARTICLE_1,
        author: String? = Authors.PRIMARY,
        score: Int? = Scores.MEDIUM,
        time: Long? = Timestamps.EPOCH_RECENT,
        title: String? = Titles.PRIMARY,
        url: String? = Urls.ARTICLE
    ) = Article(id, author, score, time, title, url)

    fun favouriteArticle(
        id: Long = Ids.ARTICLE_1,
        title: String? = Titles.FAVOURITE,
        author: String? = Authors.FAVOURITE,
        score: Int? = Scores.LOW,
        time: Long? = Timestamps.EPOCH_RECENT,
        url: String? = Urls.FAVOURITE,
        dateAdded: Long = Timestamps.NOW
    ) = FavouriteArticle(id, title, author, score, time, url, dateAdded)

    fun dbFavouriteArticle(
        id: Long = Ids.ARTICLE_1,
        title: String? = Titles.FAVOURITE,
        author: String? = Authors.FAVOURITE,
        score: Int? = Scores.LOW,
        time: Long? = Timestamps.EPOCH_RECENT,
        url: String? = Urls.FAVOURITE,
        dateAdded: Long = Timestamps.NOW
    ) = DbFavouriteArticle(id, title, author, score, time, url, dateAdded)

    fun cachedArticle(
        id: Long = Ids.ARTICLE_1,
        author: String? = Authors.PRIMARY,
        score: Int? = Scores.MEDIUM,
        time: Long? = Timestamps.EPOCH_RECENT,
        title: String? = Titles.CACHED,
        url: String? = Urls.CACHED,
        cachedAt: Long = Timestamps.NOW
    ) = CachedArticle(id, author, score, time, title, url, cachedAt)

    fun cachedTopStories(
        storyIds: List<Long> = Ids.TOP_STORIES,
        cachedAt: Long = Timestamps.NOW
    ) = CachedTopStories(storyIds = storyIds, cachedAt = cachedAt)
}
