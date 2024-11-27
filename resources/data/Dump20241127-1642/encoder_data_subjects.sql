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
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `strand` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(15) NOT NULL,
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES (1,'STEM','Oral Communication in Context','Core'),(2,'STEM','Reading and Writing Skills','Core'),(3,'STEM','21st Century Literature from the Philippines and the World','Core'),(4,'STEM','Contemporary Philippine Arts from the Regions','Core'),(5,'STEM','Komunikasyon at Pananaliksik sa Wika at Kulturang Pilipino','Core'),(6,'STEM','Pagbasa at Pagsusuri ng Iba’t-Ibang Teksto Tungo sa Pananaliksik','Core'),(7,'STEM','General Mathematics','Core'),(8,'STEM','Earth Science','Core'),(9,'STEM','Statistics and Probability','Core'),(10,'STEM','Disaster Readiness and Risk Reduction','Core'),(11,'STEM','Understanding Culture, Society and Politics','Core'),(12,'STEM','Media and Information Literacy','Core'),(13,'STEM','Personal Development','Core'),(14,'STEM','Introduction to the Philosophy of the Human Person','Core'),(15,'STEM','Physical Education and Health 1','Core'),(16,'STEM','Physical Education and Health 2','Core'),(17,'STEM','Physical Education and Health 3','Core'),(18,'STEM','Physical Education and Health 4','Core'),(19,'STEM','Empowerment Technologies','Applied'),(20,'STEM','Practical Research 1','Applied'),(21,'STEM','Practical Research 2','Applied'),(22,'STEM','Entrepreneurship','Applied'),(23,'STEM','English for Academic and Professional Purposes','Applied'),(24,'STEM','Pagsulat sa Filipino sa Piling Larangan (Akademik)','Applied'),(25,'STEM','Inquiries, Investigations and Immersion','Applied'),(26,'STEM','Pre-Calculus','Specialized'),(27,'STEM','Basic Calculus','Specialized'),(28,'STEM','General Chemistry 1','Specialized'),(29,'STEM','General Chemistry 2','Specialized'),(30,'STEM','General Biology 1','Specialized'),(31,'STEM','General Biology 2','Specialized'),(32,'STEM','General Physics 1','Specialized'),(33,'STEM','General Physics 2','Specialized'),(34,'STEM','Research/Capstone Project','Specialized');
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-27 16:42:05
