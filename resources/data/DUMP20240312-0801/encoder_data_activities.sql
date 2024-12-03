-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: encoder_data
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `activity_id` int NOT NULL AUTO_INCREMENT,
  `class_id` int NOT NULL,
  `name` varchar(30) NOT NULL,
  `total_score` int NOT NULL,
  `activity_type_id` int NOT NULL,
  `quarter` int DEFAULT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `class_id` (`class_id`),
  KEY `activity_type_id` (`activity_type_id`),
  CONSTRAINT `activities_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`),
  CONSTRAINT `activities_ibfk_2` FOREIGN KEY (`activity_type_id`) REFERENCES `activity_types` (`activity_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` VALUES (62,1,'Quarterly Exam',50,3,1),(64,1,'Quarterly Exam',50,3,2),(101,1,'Test 1',20,1,2),(103,1,'PT 1',10,2,1),(108,1,'PT 2',10,1,2),(111,1,'3',10,1,1),(114,1,'PT 1',10,2,2),(119,10,'Quarterly Exam',50,3,1),(120,10,'Quarterly Exam',50,3,2),(121,11,'Quarterly Exam',50,3,1),(122,11,'Quarterly Exam',50,3,2),(123,12,'Quarterly Exam',50,3,1),(124,12,'Quarterly Exam',50,3,2),(125,13,'Quarterly Exam',50,3,1),(126,13,'Quarterly Exam',50,3,2),(127,14,'Quarterly Exam',50,3,1),(128,14,'Quarterly Exam',50,3,2),(129,14,'Seatwork 1',10,1,1),(130,14,'Performance Task 1',10,2,1),(131,14,'Seatwork 1',10,1,2),(132,14,'Group Report',10,2,2),(133,13,'Assignment #1',10,1,1),(134,13,'Long Quiz',30,2,1),(136,13,'Seatwork #1',10,1,2),(137,13,'Quiz 1',10,1,2);
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-03  8:02:02
