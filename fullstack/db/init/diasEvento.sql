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

CREATE TABLE `feedback_response` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `edad` int NOT NULL,
  `rol` varchar(100) DEFAULT NULL,
  `area_estudio` varchar(255) DEFAULT NULL,

  `calificacion_registro` varchar(50) DEFAULT NULL,
  `comentario` varchar(255) DEFAULT NULL,
  `calificacion_organizacion` varchar(50) DEFAULT NULL,
  `calificacion_presentaciones` varchar(50) DEFAULT NULL,
  `calificacion_duracion` varchar(50) DEFAULT NULL,
  `calificacion_claridad` varchar(50) DEFAULT NULL,
  `calificacion_interaccion` varchar(50) DEFAULT NULL,
  `calificacion_instalaciones` varchar(50) DEFAULT NULL,
  `calificacion_recursos` varchar(50) DEFAULT NULL,
  `calificacion_break` varchar(50) DEFAULT NULL,

  `aspectos_positivos` varchar(255) DEFAULT NULL,
  `aspectos_a_mejorar` varchar(255) DEFAULT NULL,
  `sugerencia_especifica` varchar(255) DEFAULT NULL,

  `event_id` int NOT NULL,

  PRIMARY KEY (`id`),
  KEY `fk_feedback_event` (`event_id`),
  CONSTRAINT `fk_feedback_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
);