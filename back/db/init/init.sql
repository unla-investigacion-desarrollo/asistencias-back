CREATE SCHEMA IF NOT EXISTS `asistencias` DEFAULT CHARACTER SET utf8 ; 
SET NAMES utf8;
USE `asistencias` ;

--
-- Table structure for table `internal_user`
--

CREATE TABLE `internal_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `usuario`
--
INSERT INTO asistencias.internal_user
(active, created_at, email, last_name, name, password, `role`, updated_at)
VALUES(1, '2024-06-26 00:00:00', 'admin@gmail.com', 'admin', 'admin', '$2a$10$dAUhLKAQjiAgo9HohwvcGeeOg2nYXa/QTAB464GPyo8ZvsePiyuDe', 'ADMIN', '2024-06-26 00:00:00');

--
-- Table structure for table `event`
--

CREATE TABLE `event` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `public_form_link` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `assistance_response`
--

CREATE TABLE `assistance_response` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qrcode` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `document_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_assistance_certify_sent` bit(1) NOT NULL,
  `is_present` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `event_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKonoqp60wntimejd3in1cadl1w` (`event_id`),
  CONSTRAINT `FKonoqp60wntimejd3in1cadl1w` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;