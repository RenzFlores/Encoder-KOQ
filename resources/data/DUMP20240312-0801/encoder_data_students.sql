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
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `lrn` bigint DEFAULT NULL,
  `gender` varchar(10) NOT NULL,
  `date_of_birth` date NOT NULL,
  `strand` varchar(20) NOT NULL,
  `grade_level` int DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'Renz Ken','Tomagan','Flores',136543090290,'Male','2003-09-02','STEM',11,'rkflores@gmail.com','password'),(2,'Lorenz','Mijares','Chiong',182756102489,'Male','2004-01-01','STEM',11,'lchiong@gmail.com','password'),(3,'Richard Daniel',NULL,'Sarmiento',136745102848,'Male','2004-01-01','STEM',11,'rdsarmiento@gmail.com','password'),(4,'Darwin','Garnizo','Lapid',158275102787,'Male','2005-05-07','STEM',11,'dlapid@gmail.com','password'),(5,'Juan',NULL,'Dela Cruz',118273081723,'Male','2002-12-01','STEM',11,'jdelacruz@gmail.com','password'),(6,'Maria','Santos','Castillo',134516102746,'Female','2004-05-05','STEM',11,'mcastillo@gmail.com','password'),(7,'Alliyah','Cruz','Reyes',125755101273,'Female','2004-01-12','STEM',11,'areyes@gmail.com','password'),(8,'Kaye','Torres','Santiago',173648102746,'Female','2004-02-21','STEM',11,'ksantiago@gmail.com','password'),(9,'Coline','Miranda','Diaz',114756101745,'Female','2003-08-12','STEM',11,'cdiaz@gmail.com','password'),(10,'Rose','Ching','Villena',147264100293,'Female','2005-11-10','STEM',11,'rvillena@gmail.com','password'),(11,'Test','Test','Test',12345,'Male','2024-01-01','STEM',11,'test@gmail.com','password'),(12,'',NULL,'',NULL,'','0000-00-00','',NULL,NULL,'');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-03  8:02:01
