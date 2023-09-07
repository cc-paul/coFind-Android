package com.jmr.data

data class MessageResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: MessageDataMe
)

data class MessageDataMe(
    val rows_returned: Int,
    val message: List<MessageItem>
)

data class MessageItem(
    val messageText: String,
    val isMine: Int,
    val isRead: Int
)