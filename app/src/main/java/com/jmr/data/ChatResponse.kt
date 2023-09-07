package com.jmr.data

data class ChatResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: MessageChatData
)

data class MessageChatData(
    val rows_returned: Int,
    val message: List<MessageChatItem>
)

data class MessageChatItem(
    val contact_name: String,
    val last_message: String,
    val imageLink: String,
    val receiver_id: Int,
    val sender_id: Int
)