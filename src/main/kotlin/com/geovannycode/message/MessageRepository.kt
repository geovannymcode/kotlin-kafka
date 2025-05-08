package com.geovannycode.message

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class MessageRepository(private val jdbcTemplate: JdbcTemplate) {
    private val logger = LoggerFactory.getLogger(MessageRepository::class.java)


    fun save(message: Message): Boolean {
        return try {
            jdbcTemplate.update(
                """
                INSERT INTO messages (message_id, content, created_at)
                VALUES (?, ?, ?)
                """,
                message.messageId,
                message.content,
                Timestamp(message.timestamp)
            )
            logger.debug("Mensaje guardado en base de datos: ID={}", message.messageId)
            true
        } catch (e: Exception) {
            logger.error("Error al guardar mensaje en base de datos: {}", e.message, e)
            false
        }
    }

    fun findById(messageId: String): Message? {
        return try {
            jdbcTemplate.queryForObject(
                """
                SELECT message_id, content, created_at
                FROM messages
                WHERE message_id = ?
                """,
                { rs, _ ->
                    Message(
                        content = rs.getString("content"),
                        messageId = rs.getString("message_id"),
                        timestamp = rs.getTimestamp("created_at").time
                    )
                },
                messageId
            )
        } catch (e: Exception) {
            logger.error("Error al buscar mensaje con ID {}: {}", messageId, e.message)
            null
        }
    }

    fun findAll(): List<Message> {
        return try {
            jdbcTemplate.query(
                """
                SELECT message_id, content, created_at
                FROM messages
                ORDER BY created_at DESC
                """,
                { rs, _ ->
                    Message(
                        content = rs.getString("content"),
                        messageId = rs.getString("message_id"),
                        timestamp = rs.getTimestamp("created_at").time
                    )
                }
            )
        } catch (e: Exception) {
            logger.error("Error al recuperar todos los mensajes: {}", e.message, e)
            emptyList()
        }
    }
}