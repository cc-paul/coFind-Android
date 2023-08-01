package com.jmr.data

data class BaseResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: List<String>
)