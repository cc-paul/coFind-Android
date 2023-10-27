package com.jmr.data

data class ReviewResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: ReviewData
)

data class ReviewData(
    val rows_returned: Int,
    val review: List<Review>
)

data class Review(
    val id: Int,
    val review: String,
    val starsCount: Int,
    val fullName: String,
    val imageLink: String
)