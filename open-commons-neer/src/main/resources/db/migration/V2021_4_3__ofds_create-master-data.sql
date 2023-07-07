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
-- Dumping data for table `ofds_function`
--
-- ORDER BY:  `id`

LOCK TABLES `ofds_function` WRITE;
/*!40000 ALTER TABLE `ofds_function` DISABLE KEYS */;
INSERT INTO `ofds_function` 
	(`id`, `function_name`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(1,'FIND USERS',1,1,now(),NULL,NULL),
	(2,'CREATE USERS',1,1,now(),NULL,NULL);
/*!40000 ALTER TABLE `ofds_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ofds_group_function`
--
-- ORDER BY:  `id`

LOCK TABLES `ofds_group_function` WRITE;
/*!40000 ALTER TABLE `ofds_group_function` DISABLE KEYS */;
INSERT INTO `ofds_group_function` 
	(`id`, `group_id`, `function_id`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(1,1,1,NULL,NULL,now(),NULL,NULL),
	(2,1,2,NULL,NULL,now(),NULL,NULL);
/*!40000 ALTER TABLE `ofds_group_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ofds_group`
--
-- ORDER BY:  `id`

LOCK TABLES `ofds_group` WRITE;
/*!40000 ALTER TABLE `ofds_group` DISABLE KEYS */;
INSERT INTO `ofds_group` 
	(`id`, `group_name`, `description`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(1,'User Admin Group','This is user admin group',1,1,now(), NULL, NULL);
/*!40000 ALTER TABLE `ofds_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ofds_user`
--
-- ORDER BY:  `id`

LOCK TABLES `ofds_user` WRITE;
/*!40000 ALTER TABLE `ofds_user` DISABLE KEYS */;
INSERT INTO `ofds_user` 
	(`id`, `username`, `password`, `description`, `first_name`, `last_name`, `is_super_admin`, `gender`, `email`, `mobile`, `phone`, `status`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(1,'admin','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', NULL, 'Admin', NULL,1,'male','praveenatwork2018@gmail.com', NULL, NULL, 'active',1,1, now(), NULL, NULL);
INSERT INTO `ofds_user` 
	(`id`, `username`, `password`, `description`, `first_name`, `last_name`, `is_super_admin`, `gender`, `email`, `mobile`, `phone`, `status`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(2,'praveen','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', NULL, 'Praveen Kumar', 'Mishra',0,'male','praveenatwork2018@gmail.com', NULL, NULL, 'active',1,1, now(), NULL, NULL);
INSERT INTO `ofds_user` 
	(`id`, `username`, `password`, `description`, `first_name`, `last_name`, `is_super_admin`, `gender`, `email`, `mobile`, `phone`, `status`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(3,'nandni','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', NULL, 'Nandni', 'Shree',0,'female','nandni@gmail.com', NULL, NULL, 'active',1,1, now(), NULL, NULL);
INSERT INTO `ofds_user` 
	(`id`, `username`, `password`, `description`, `first_name`, `last_name`, `is_super_admin`, `gender`, `email`, `mobile`, `phone`, `status`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(4,'yash','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', NULL, 'Yash', 'Mishra',0,'male','yash@gmail.com', NULL, NULL, 'active',1,1, now(), NULL, NULL);
INSERT INTO `ofds_user` 
	(`id`, `username`, `password`, `description`, `first_name`, `last_name`, `is_super_admin`, `gender`, `email`, `mobile`, `phone`, `status`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(5,'anshika','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', NULL, 'Anshika', 'Mishra',0,'female','anshika@gmail.com', NULL, NULL, 'active',1,1, now(), NULL, NULL);
/*!40000 ALTER TABLE `ofds_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ofds_user_group`
--
-- ORDER BY:  `id`

LOCK TABLES `ofds_user_group` WRITE;
/*!40000 ALTER TABLE `ofds_user_group` DISABLE KEYS */;
INSERT INTO `ofds_user_group` 
	(`id`, `group_id`, `user_id`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	(1,1,2,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `ofds_user_group` ENABLE KEYS */;
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
