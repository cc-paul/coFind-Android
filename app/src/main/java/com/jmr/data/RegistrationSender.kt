package com.jmr.data

import com.google.gson.annotations.SerializedName

data class RegistrationSender(
    @SerializedName("firstName") val firstName : String,
    @SerializedName("middleName") val middleName : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("username") val username : String,
    @SerializedName("emailAddress") val emailAddress : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("address") val address : String,
    @SerializedName("password") val password : String,
    @SerializedName("rPassword") val rPassword : String,
    @SerializedName("isGoogleSignIn") val isGoogleSignIn : String
)
