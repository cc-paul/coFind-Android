package com.jmr.data

import com.google.gson.annotations.SerializedName

data class LoginSender(
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String
)
