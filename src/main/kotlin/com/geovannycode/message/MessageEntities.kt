package com.geovannycode.message

import java.time.Instant

data class CustomMessage(
    val content: String,
    val messageId: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)
