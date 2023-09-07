package com.jmr.data

import com.google.gson.annotations.SerializedName

data class MessageSender(
    @SerializedName("sender_id") val senderId : Int = 0,
    @SerializedName("receiver_id") val receiverId : Int  = 0,
    @SerializedName("message_text") val messageText : String = "",
)
