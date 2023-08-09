package com.jmr.data

import com.google.gson.annotations.SerializedName

data class ProfileSender(
    @SerializedName("command") val command : String = "NOTHING",
    @SerializedName("user_id") val user_id : Int = 0,
    @SerializedName("imageLink") val imageLink : String = "",
    @SerializedName("password") val password : String = "",
    @SerializedName("firstName") val firstName : String = "",
    @SerializedName("middleName") val middleName : String = "",
    @SerializedName("lastName") val lastName : String = "",
    @SerializedName("mobileNumber") val mobileNumber : String = "",
    @SerializedName("address") val address : String = ""
)
