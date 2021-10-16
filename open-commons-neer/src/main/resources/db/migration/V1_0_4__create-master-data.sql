-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: neerdev2
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Dumping data for table `functions`
--
-- ORDER BY:  `id`

LOCK TABLES `functions` WRITE;
/*!40000 ALTER TABLE `functions` DISABLE KEYS */;
INSERT INTO `functions` 
	(`id`, `created_date`, `is_valid`, `modified_date`, `function_name`, `created_by`, `modified_by`) 
VALUES 
	(1,now(),1,NULL,'FIND USERS',1,NULL),
	(2,now(),1,NULL,'CREATE USERS',1,NULL);
/*!40000 ALTER TABLE `functions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `group_function`
--
-- ORDER BY:  `id`

LOCK TABLES `group_function` WRITE;
/*!40000 ALTER TABLE `group_function` DISABLE KEYS */;
INSERT INTO `group_function` 
	(`id`, `created_date`, `is_valid`, `modified_date`, `created_by`, `modified_by`, `function_id`, `group_id`) 
VALUES 
	(1,NULL,NULL,NULL,NULL,NULL,1,1),
	(2,NULL,NULL,NULL,NULL,NULL,2,1);
/*!40000 ALTER TABLE `group_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `groupe`
--
-- ORDER BY:  `id`

LOCK TABLES `groupe` WRITE;
/*!40000 ALTER TABLE `groupe` DISABLE KEYS */;
INSERT INTO `groupe` 
	(`id`, `created_date`, `is_valid`, `modified_date`, `description`, `group_name`, `created_by`, `modified_by`) 
VALUES 
	(1,now(),1,'2020-09-27 21:28:22','This is user admin group','User Admin Group',1,1);
/*!40000 ALTER TABLE `groupe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--
-- ORDER BY:  `id`

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` 
	(`id`, `created_date`, `is_valid`, `modified_date`, `description`, `email`, `first_name`, `gender`, `is_super_admin`, `last_name`, `mobile`, `password`, `phone`, `status`, `username`, `created_by`, `modified_by`) 
VALUES 
	(1,now(),1,NULL,NULL,'praveenatwork2018@gmail.com','Admin','male',_binary '',NULL,NULL,'5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8',NULL,'active','admin',1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_group`
--
-- ORDER BY:  `id`

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` 
	(`id`, `created_date`, `is_valid`, `modified_date`, `created_by`, `modified_by`, `group_id`, `user_id`) 
VALUES 
	(1,NULL,NULL,NULL,NULL,NULL,1,2);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-01 17:53:56
