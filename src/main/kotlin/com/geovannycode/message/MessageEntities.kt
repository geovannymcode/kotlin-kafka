package com.geovannycode.message

import java.time.Instant

data class Message(
    val content: String,
    val messageId: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)
