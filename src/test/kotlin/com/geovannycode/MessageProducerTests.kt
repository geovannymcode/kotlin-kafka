package com.geovannycode

import com.geovannycode.message.MessageProducer
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.kafka.core.KafkaTemplate

class MessageProducerTests {

    private val topicName = "test-topic"
    private val kafkaTemplate = mock<KafkaTemplate<String, String>>()
    private val messageProducer = MessageProducer(kafkaTemplate, topicName)

    @Test
    fun `cuando se envía un mensaje, se debe publicar en Kafka`() {
        // Given
        val content = "Mensaje de prueba"

        // When
        messageProducer.sendMessage(content)

        // Then
        verify(kafkaTemplate).send(eq(topicName), any())
    }
}