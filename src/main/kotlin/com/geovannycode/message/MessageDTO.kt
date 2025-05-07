package com.geovannycode.message

data class MessageRequest(
    val content: String
)


data class MessageResponse(
    val id: String,
    val content: String,
    val timestamp: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null,
    val error: String? = null
)
