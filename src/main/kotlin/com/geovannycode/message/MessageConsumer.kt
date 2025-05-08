package com.geovannycode.message

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class MessageConsumer(private val messageRepository: MessageRepository) {
    private val logger = LoggerFactory.getLogger(MessageConsumer::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    @KafkaListener(
        topics = ["\${spring.kafka.topic.name}"],
        groupId = "\${spring.kafka.consumer.group-id}",
        errorHandler = "kafkaErrorHandler"
    )
    fun processMessage(message: Message) {
        logger.info("Mensaje recibido: {}, ID: {}, Tiempo: {}",
            message.content, message.messageId, message.timestamp)

        try {

            val formattedTime = dateFormatter.format(Instant.ofEpochMilli(message.timestamp))
            val processedMessage = message.copy(content = message.content.uppercase())
            messageRepository.save(processedMessage)

            logger.info("Mensaje procesado exitosamente a las {}", formattedTime)
        } catch (e: Exception) {
            logger.error("Error al procesar el mensaje: {}", e.message, e)
        }
    }
}