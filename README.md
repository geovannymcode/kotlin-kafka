# Arquitectura Orientada a Eventos con Kotlin, Spring Boot y Kafka

Este proyecto demuestra cómo construir una arquitectura orientada a eventos utilizando **Kotlin**, **Spring Boot** y **Apache Kafka**, siguiendo buenas prácticas de diseño y pruebas. Incluye componentes clave como productor y consumidor de Kafka, persistencia con H2, endpoints RESTful y pruebas automatizadas.

## 🧱 Tecnologías Utilizadas

| Componente          | Versión / Función                                   |
|---------------------|-----------------------------------------------------|
| Kotlin              | 2.1.0 – Lenguaje principal                          |
| Java                | 21 – Versión LTS de la JVM                          |
| Spring Boot         | 3.4.5 – Framework de desarrollo web y empresarial   |
| Apache Kafka        | Plataforma de mensajería distribuida                |
| Kafkalytic          | Plugin de JetBrains para testeo e inspección de Kafka |
| Docker              | Contenerización de Kafka + Zookeeper                |
| H2                  | Base de datos embebida para desarrollo y testing    |

## 📦 Estructura del Proyecto

```
src/
├── main/
│   ├── kotlin/com/geovannycode/
│   │   ├── KotlinKafkaApplication.kt
│   │   ├── config/
│   │   │   ├── DatabaseConfig.kt
│   │   │   └── MessageConfig.kt
│   │   ├── message/
│   │   │   ├── MessageConsumer.kt
│   │   │   ├── MessageDTO.kt
│   │   │   ├── MessageEntities.kt
│   │   │   ├── MessageProducer.kt
│   │   │   └── MessageRepository.kt
│   │   └── web/controller/
│   │       └── MessageController.kt
│   └── resources/
│       ├── application.yaml
│       └── schema.sql
├── test/kotlin/com/geovannycode/
│   ├── KafkaIntegrationTests.kt
│   ├── KotlinKafkaApplicationTests.kt
│   └── MessageProducerTests.kt
```

## ⚙️ Configuración

### Kafka + Zookeeper vía Docker Compose

Archivo `compose.yml`:

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.6.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

### `application.yaml`

```yaml
spring:
  application:
    name: kotlin-kafka
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: geovannycode-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    topic:
      name: geovannycode-topic

  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:

h2:
  console:
    enabled: true
    path: /h2-console

sql:
  init:
    mode: always
```

## 🚀 Cómo Ejecutar

1. Ejecutar la aplicación:
   ```bash
   ./gradlew bootRun
   ```

2. Probar endpoints con [Postman](https://www.postman.com/) o similar:
    - Enviar mensaje: `POST /api/messages`
    - Consultar mensajes: `GET /api/messages`
    - Consultar por ID: `GET /api/messages/{id}`

## 🧪 Pruebas

- Ejecutar pruebas unitarias e integración:
  ```bash
  ./gradlew test
  ```

- Las pruebas cubren:
    - Envío de mensajes a Kafka (`MessageProducerTests.kt`)
    - Flujo completo productor-consumidor (`KafkaIntegrationTests.kt`)
    - Test de contexto (`KotlinKafkaApplicationTests.kt`)

## 🧰 Herramientas Recomendadas

### 🔌 Kafkalytic para IntelliJ IDEA

- Plugin de JetBrains para trabajar con Kafka desde el IDE
- Instalación:
  ```
  File → Settings → Plugins → Marketplace → Kafkalytic
  ```

- Funcionalidades:
    - Visualización de tópicos, particiones y offsets
    - Envío de mensajes en masa
    - Consumo y depuración de mensajes

## 📚 Referencias

- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Apache Kafka Docs](https://kafka.apache.org/documentation/)
- [Kafkalytic Plugin](https://plugins.jetbrains.com/plugin/11946-kafkalytic)

## 🧑‍💻 Autor

**Geovanny Mendoza**

- 🌐 [geovannycode.com](https://www.geovannycode.com)
- 🐦 [@geovannycode](https://twitter.com/geovannycode)
- 💼 [LinkedIn](https://www.linkedin.com/in/geovannycode)
