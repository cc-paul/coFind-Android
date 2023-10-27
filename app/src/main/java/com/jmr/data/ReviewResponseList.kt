package com.jmr.data

data class ReviewResponseList(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: ReviewDataList
)

data class ReviewDataList(
    val rows_returned: Int,
    val review: List<ReviewList>
)

data class ReviewList(
    val fullName: String,
    val review: String,
    val starsCount: Int,
    val imageLink: String
)