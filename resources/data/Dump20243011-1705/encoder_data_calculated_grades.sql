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
-- Table structure for table `calculated_grades`
--

DROP TABLE IF EXISTS `calculated_grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calculated_grades` (
  `calculated_grade_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `class_id` int NOT NULL,
  `q1_ww_ps` decimal(5,2) DEFAULT NULL,
  `q1_pt_ps` decimal(5,2) DEFAULT NULL,
  `q1_qa_ps` decimal(5,2) DEFAULT NULL,
  `q2_ww_ps` decimal(5,2) DEFAULT NULL,
  `q2_pt_ps` decimal(5,2) DEFAULT NULL,
  `q2_qa_ps` decimal(5,2) DEFAULT NULL,
  `q1_raw_grade` decimal(5,2) DEFAULT NULL,
  `q2_raw_grade` decimal(5,2) DEFAULT NULL,
  `sem_raw_avg` decimal(5,2) DEFAULT NULL,
  `remarks` varchar(10) DEFAULT NULL,
  `description` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`calculated_grade_id`),
  KEY `calculated_grades_ibfk_2_idx` (`class_id`),
  KEY `calculated_grades_ibfk_1_idx` (`student_id`),
  CONSTRAINT `calculated_grades_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `calculated_grades_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calculated_grades`
--

LOCK TABLES `calculated_grades` WRITE;
/*!40000 ALTER TABLE `calculated_grades` DISABLE KEYS */;
INSERT INTO `calculated_grades` VALUES (1,1,1,80.00,100.00,84.00,76.67,80.00,76.00,91.00,78.17,NULL,NULL,NULL),(3,3,1,70.00,80.00,72.00,63.33,70.00,54.00,75.50,64.33,NULL,NULL,NULL),(4,6,1,60.00,90.00,82.00,56.67,90.00,50.00,80.50,71.67,NULL,NULL,NULL),(5,4,1,80.00,80.00,58.00,76.67,60.00,78.00,74.50,68.67,NULL,NULL,NULL),(6,5,1,100.00,100.00,74.00,76.67,80.00,82.00,93.50,79.67,NULL,NULL,NULL),(10,2,1,50.00,40.00,66.00,83.33,80.00,66.00,49.00,77.33,NULL,NULL,NULL),(11,1,9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,1,14,80.00,90.00,58.00,80.00,100.00,66.00,79.50,86.50,NULL,NULL,NULL),(14,7,14,60.00,90.00,82.00,90.00,90.00,72.00,80.50,85.50,NULL,NULL,NULL),(15,6,14,50.00,60.00,70.00,80.00,100.00,78.00,60.00,89.50,NULL,NULL,NULL),(16,4,14,80.00,70.00,92.00,70.00,90.00,70.00,78.00,80.00,NULL,NULL,NULL),(17,1,13,100.00,63.33,82.00,70.00,NULL,76.00,77.17,36.50,NULL,NULL,NULL),(18,4,13,100.00,90.00,90.00,90.00,NULL,80.00,92.50,42.50,NULL,NULL,NULL),(19,6,13,90.00,70.00,78.00,75.00,NULL,84.00,77.00,39.75,NULL,NULL,NULL),(20,7,13,80.00,80.00,72.00,85.00,NULL,88.00,78.00,43.25,NULL,NULL,NULL);
/*!40000 ALTER TABLE `calculated_grades` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-30 17:06:04