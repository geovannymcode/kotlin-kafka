package com.geovannycode.message

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class MessageConsumer(private val messageRepository: MessageRepository) {
    private val logger = LoggerFactory.getLogger(MessageConsumer::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    @KafkaListener(
        topics = ["\${spring.kafka.topic.name}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listen(@Payload content: String) {
        logger.info("Mensaje recibido: {}", content)

        try {

            val message = CustomMessage(
                content = content.uppercase(),
                messageId = UUID.randomUUID().toString()
            )

            val instant = Instant.ofEpochMilli(message.timestamp)
            val formattedTime = dateFormatter.format(instant)

            messageRepository.save(message)

            logger.info("Mensaje procesado exitosamente a las {}", formattedTime)
        } catch (e: Exception) {
            logger.error("Error al procesar el mensaje: {}", e.message, e)
        }
    }
}