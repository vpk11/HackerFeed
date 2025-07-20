package com.vpk.hackerfeed.domain.model

/**
 * Domain model for an Article.
 * This represents the business model, independent of any data layer implementation.
 */
data class Article(
    val id: Long,
    val author: String?,
    val score: Int?,
    val time: Long?,
    val title: String?,
    val url: String?
)
