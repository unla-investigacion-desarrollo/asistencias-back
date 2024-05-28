CREATE SCHEMA IF NOT EXISTS `asistencias` DEFAULT CHARACTER SET utf8 ; 
SET NAMES utf8;
USE `asistencias` ;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `email` varchar(50) NOT NULL,
  `apellido` varchar(20) DEFAULT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `rol` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` VALUES ('admin@gmail.com','Admin','Admin','$2a$10$dAUhLKAQjiAgo9HohwvcGeeOg2nYXa/QTAB464GPyo8ZvsePiyuDe','ADMIN');

