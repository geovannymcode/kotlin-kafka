package com.geovannycode.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${spring.kafka.topic.name}") private val topicName: String
) {
    private val logger = LoggerFactory.getLogger(MessageProducer::class.java)

    fun sendMessage(content: String): CustomMessage {
        val message = CustomMessage(
            content = content,
            messageId = UUID.randomUUID().toString()
        )

        kafkaTemplate.send(topicName, message.content)
        logger.info("Mensaje enviado al tópico {}: {}", topicName, message.content)

        return message
    }
}