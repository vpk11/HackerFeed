package com.vpk.hackerfeed.edge

import com.vpk.hackerfeed.data.HackerNewsApiService
import com.vpk.hackerfeed.helpers.JsonFixtureReader
import com.vpk.hackerfeed.helpers.TestData
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkEdgeCaseTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: HackerNewsApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(HackerNewsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun serverError500_throwsHttpException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_SERVER_ERROR)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.ERROR_500))
        )

        val result = runCatching { api.getTopStoryIds() }

        assertTrue(result.isFailure)
    }

    @Test
    fun emptyBody_onTopStories_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody("")
        )

        val result = runCatching { api.getTopStoryIds() }

        assertTrue(result.isFailure)
    }

    @Test
    fun emptyArray_returnsEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody("[]")
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val ids = api.getTopStoryIds()

        assertTrue(ids.isEmpty())
    }

    @Test
    fun connectionTimeout_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = runCatching { api.getTopStoryIds() }

        assertTrue(result.isFailure)
    }

    @Test
    fun malformedJson_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.MALFORMED))
                .addHeader("Content-Type", TestData.Http.CONTENT_TYPE_JSON)
        )

        val result = runCatching { api.getTopStoryIds() }

        assertTrue(result.isFailure)
    }

    @Test
    fun disconnectDuringBody_throwsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(TestData.Http.STATUS_OK)
                .setBody(JsonFixtureReader.read(TestData.Fixtures.TOP_STORIES))
                .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
        )

        val result = runCatching { api.getTopStoryIds() }

        assertTrue(result.isFailure)
    }
}
