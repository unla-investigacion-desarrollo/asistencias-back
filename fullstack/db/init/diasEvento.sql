CREATE TABLE `event_days` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_event_days_event` (`event_id`),
  CONSTRAINT `fk_event_days_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
);

CREATE TABLE `assistance_days` (
  `id` int NOT NULL AUTO_INCREMENT,
  `assistance_response_id` int NOT NULL,
  `event_days_id` int NOT NULL,
  `is_present` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_days_response` (`assistance_response_id`),
  KEY `idx_days_event_days` (`event_days_id`),
  CONSTRAINT `fk_days_response` FOREIGN KEY (`assistance_response_id`) REFERENCES `assistance_response` (`id`),
  CONSTRAINT `fk_days_event_days` FOREIGN KEY (`event_days_id`) REFERENCES `event_days` (`id`)
);