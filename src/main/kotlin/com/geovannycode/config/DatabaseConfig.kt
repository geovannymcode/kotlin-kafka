package com.geovannycode.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
class DatabaseConfig {
    private val logger = LoggerFactory.getLogger(DatabaseConfig::class.java)


    @Bean
    fun dataSource(): DataSource {
        logger.info("Inicializando base de datos embebida H2")
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .build()
            .also { logger.info("Base de datos H2 inicializada correctamente") }
    }


    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        logger.info("Configurando JdbcTemplate")
        return JdbcTemplate(dataSource)
    }
}