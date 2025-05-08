package com.geovannycode

import com.geovannycode.message.CustomMessage
import com.geovannycode.message.MessageProducer
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import java.time.Duration


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = ["geovannycode-topic"])
class KafkaIntegrationTests {
    @Autowired
    private lateinit var messageProducer: MessageProducer

    @Autowired
    private lateinit var consumerFactory: ConsumerFactory<String, String>

    @Test
    fun `el mensaje enviado debe ser recibido por el consumidor`() {
        // Given
        val content = "Mensaje de integración"
        val consumer = consumerFactory.createConsumer("test-group", "auto.offset.reset=earliest")
        consumer.subscribe(listOf("geovannycode-topic"))

        // When
        messageProducer.sendMessage(content)

        // Then
        await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted {
                val records = KafkaTestUtils.getRecords(consumer)
                assert(records.count() > 0)
                val record = records.first()
                assert(record.value() == content)
            }
    }
}