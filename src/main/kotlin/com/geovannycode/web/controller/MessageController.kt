package com.geovannycode.web.controller

import com.geovannycode.message.ApiResponse
import com.geovannycode.message.Message
import com.geovannycode.message.MessageProducer
import com.geovannycode.message.MessageRepository
import com.geovannycode.message.MessageRequest
import com.geovannycode.message.MessageResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val messageProducer: MessageProducer,
    private val messageRepository: MessageRepository
) {
    private val logger = LoggerFactory.getLogger(MessageController::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())


    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun sendMessage(@RequestBody request: MessageRequest): ResponseEntity<ApiResponse> {
        logger.debug("Solicitud recibida para enviar mensaje: {}", request.content)

        return try {
            val message = messageProducer.sendMessage(request.content)
            ResponseEntity.ok(ApiResponse(
                success = true,
                message = "Mensaje enviado correctamente",
                data = mapToResponse(message)
            ))
        } catch (e: Exception) {
            logger.error("Error al enviar mensaje: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse(
                    success = false,
                    message = "Error al enviar mensaje",
                    error = e.message
                ))
        }
    }


    @GetMapping
    fun getAllMessages(): ResponseEntity<ApiResponse> {
        logger.debug("Solicitud recibida para obtener todos los mensajes")

        val messages = messageRepository.findAll()
        val messageResponses = messages.map { mapToResponse(it) }

        return ResponseEntity.ok(ApiResponse(
            success = true,
            message = "Mensajes recuperados con éxito",
            data = messageResponses
        ))
    }

    @GetMapping("/{id}")
    fun getMessageById(@PathVariable id: String): ResponseEntity<ApiResponse> {
        logger.debug("Solicitud recibida para obtener mensaje con ID: {}", id)

        val message = messageRepository.findById(id)

        return if (message != null) {
            ResponseEntity.ok(ApiResponse(
                success = true,
                message = "Mensaje recuperado con éxito",
                data = mapToResponse(message)
            ))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse(
                    success = false,
                    message = "Mensaje no encontrado",
                    error = "No existe mensaje con ID: $id"
                ))
        }
    }


    private fun mapToResponse(message: Message): MessageResponse {
        return MessageResponse(
            id = message.messageId,
            content = message.content,
            timestamp = dateFormatter.format(Instant.ofEpochMilli(message.timestamp))
        )
    }
}