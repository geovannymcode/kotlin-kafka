package com.geovannycode.message

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, Message>,
    @Value("\${spring.kafka.topic.name}") private val topicName: String
) {
    private val logger = LoggerFactory.getLogger(MessageProducer::class.java)

    /**
     * Envía un mensaje al tópico de Kafka
     */
    fun sendMessage(content: String): Message {
        val message = Message(
            content = content,
            messageId = UUID.randomUUID().toString()
        )

        kafkaTemplate.send(topicName, message)
        logger.info("Mensaje enviado al tópico {}: {}", topicName, message)

        return message
    }
}