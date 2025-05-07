CREATE TABLE IF NOT EXISTS messages (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        message_id VARCHAR(36) NOT NULL,
                                        content VARCHAR(500) NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT uk_message_id UNIQUE (message_id)
);

CREATE INDEX IF NOT EXISTS idx_message_id ON messages (message_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON messages (created_at);