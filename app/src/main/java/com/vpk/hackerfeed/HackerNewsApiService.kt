package com.vpk.hackerfeed

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class Article(
    val id: Long,
    @SerializedName("by") val author: String?,
    val score: Int?,
    val time: Long?,
    val title: String?,
    val url: String?
)

// Retrofit interface defining the API endpoints
interface HackerNewsApiService {
    @GET("v0/topstories.json")
    suspend fun getTopStoryIds(): List<Long>

    @GET("v0/item/{id}.json")
    suspend fun getArticleDetails(@Path("id") id: Long): Article
}

object RetrofitInstance {
    private const val BASE_URL = "https://hacker-news.firebaseio.com/"

    val api: HackerNewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HackerNewsApiService::class.java)
    }
}