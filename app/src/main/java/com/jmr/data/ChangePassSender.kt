package com.jmr.data

import com.google.gson.annotations.SerializedName

data class ChangePassSender(
    @SerializedName("emailAddress") val emailAddress : String,
    @SerializedName("password") val password : String
)
