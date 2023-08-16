package com.jmr.data

data class ProfileResponse (
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: UserData
)

data class UserData(
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val emailAddress: String = "",
    val mobileNumber: String = "",
    val address: String = "",
    val username: String = "",
    val imageLink: String = ""
)