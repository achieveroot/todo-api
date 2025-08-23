CREATE TABLE IF NOT EXISTS `todos` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `public_id` BINARY(16) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `completed` TINYINT(1) NOT NULL DEFAULT 0,
  `priority` VARCHAR(20) NOT NULL DEFAULT 'NONE',
  `deadline` DATETIME(6) DEFAULT NULL,
  CONSTRAINT `pk_todos` PRIMARY KEY (`id`),
  CONSTRAINT `uk_todos_public_id` UNIQUE KEY (`public_id`),
  CONSTRAINT `ck_todos_priority` CHECK (`priority` IN ('HIGH','MEDIUM','LOW','NONE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
