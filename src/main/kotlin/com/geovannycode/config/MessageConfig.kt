package com.geovannycode.config

import org.apache.kafka.clients.admin.NewTopic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.listener.KafkaListenerErrorHandler

@Configuration
class MessageConfig {
    private val logger = LoggerFactory.getLogger(MessageConfig::class.java)

    @Value("\${spring.kafka.topic.name}")
    private lateinit var topicName: String

    @Bean
    fun messageTopic(): NewTopic {
        logger.info("Creando tópico Kafka: {}", topicName)
        return TopicBuilder
            .name(topicName)
            .partitions(1)
            .replicas(1)
            .build()
            .also { logger.info("Tópico {} configurado correctamente", topicName) }
    }
    @Bean
    fun kafkaErrorHandler(): KafkaListenerErrorHandler {
        return KafkaListenerErrorHandler { message, exception ->
            logger.error("Error al procesar mensaje: {}", message.payload)
            logger.error("Excepción: {}", exception.cause?.message, exception.cause)
        }
    }
}