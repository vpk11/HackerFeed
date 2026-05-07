package com.vpk.hackerfeed.data.api

import com.vpk.hackerfeed.data.HackerNewsApiService
import com.vpk.hackerfeed.helpers.JsonFixtureReader
import com.vpk.hackerfeed.helpers.TestData
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class HackerNewsApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: HackerNewsApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HackerNewsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getTopStoryIds_parsesJsonArray() = runTest {
        val body = JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(body)
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val ids = api.getTopStoryIds()

        assertEquals(TestData.Ids.TOP_STORIES, ids)
    }

    @Test
    fun getArticleDetails_parsesFullArticle() = runTest {
        val body = JsonFixtureReader.read(TestData.Fixtures.ARTICLE_DETAIL)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(body)
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val article = api.getArticleDetails(TestData.Ids.ARTICLE_1)

        assertEquals(TestData.Ids.ARTICLE_1, article.id)
        assertEquals(TestData.Authors.PRIMARY, article.author)
        assertEquals(TestData.Scores.HIGH, article.score)
        assertEquals(TestData.Titles.PRIMARY, article.title)
        assertEquals(TestData.Urls.ARTICLE, article.url)
    }

    @Test
    fun getArticleDetails_parsesMinimalArticle() = runTest {
        val body = JsonFixtureReader.read(TestData.Fixtures.ARTICLE_DETAIL_MINIMAL)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(body)
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val article = api.getArticleDetails(TestData.Ids.ARTICLE_2)

        assertEquals(TestData.Ids.ARTICLE_2, article.id)
        assertNull(article.author)
        assertNull(article.score)
        assertNull(article.url)
        assertNotNull(article.title)
    }

    @Test(expected = Exception::class)
    fun getTopStoryIds_404_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_NOT_FOUND)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.ERROR_404))
        )

        api.getTopStoryIds()
    }

    @Test(expected = Exception::class)
    fun getArticleDetails_500_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_SERVER_ERROR)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.ERROR_500))
        )

        api.getArticleDetails(TestData.Ids.ARTICLE_1)
    }

    @Test(expected = Exception::class)
    fun getTopStoryIds_malformedJson_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.MALFORMED))
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        api.getTopStoryIds()
    }

    @Test(expected = Exception::class)
    fun getTopStoryIds_timeout_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val timeoutApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .readTimeout(1, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(HackerNewsApiService::class.java)

        timeoutApi.getTopStoryIds()
    }

    @Test
    fun getTopStoryIds_requestPath_isCorrect() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
        )

        api.getTopStoryIds()

        val request = mockWebServer.takeRequest()
        assertEquals("/v0/topstories.json", request.path)
    }

    @Test
    fun getNewStoryIds_requestPath_isCorrect() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
        )

        api.getNewStoryIds()

        val request = mockWebServer.takeRequest()
        assertEquals("/v0/newstories.json", request.path)
    }

    @Test
    fun getBestStoryIds_requestPath_isCorrect() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
        )

        api.getBestStoryIds()

        val request = mockWebServer.takeRequest()
        assertEquals("/v0/beststories.json", request.path)
    }

    @Test
    fun getNewStoryIds_parsesJsonArray() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val ids = api.getNewStoryIds()

        assertEquals(TestData.Ids.TOP_STORIES, ids)
    }

    @Test
    fun getBestStoryIds_parsesJsonArray() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val ids = api.getBestStoryIds()

        assertEquals(TestData.Ids.TOP_STORIES, ids)
    }

    @Test
    fun getArticleDetails_requestPath_containsId() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.ARTICLE_DETAIL))
        )

        api.getArticleDetails(TestData.Ids.ARTICLE_1)

        val request = mockWebServer.takeRequest()
        assertEquals("/v0/item/${TestData.Ids.ARTICLE_1}.json", request.path)
    }
}
