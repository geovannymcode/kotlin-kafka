package com.geovannycode.message

import java.time.Instant

data class Message(
    val message: String,
    val messageId: Int,
    val timestamp: Long = Instant.now().toEpochMilli()
)
