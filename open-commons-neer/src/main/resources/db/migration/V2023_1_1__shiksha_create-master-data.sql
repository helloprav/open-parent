LOCK TABLES `ofds_group` WRITE;
/*!40000 ALTER TABLE `ofds_group` DISABLE KEYS */;
INSERT INTO `ofds_group` 
	(`group_name`, `description`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	('Grade 1','This is group for users in grade 1',1,1,now(), NULL, NULL);
INSERT INTO `ofds_group` 
	(`group_name`, `description`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	('Grade 2','This is group for users in grade 2',1,1,now(), NULL, NULL);
INSERT INTO `ofds_group` 
	(`group_name`, `description`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	('Grade 3','This is group for users in grade 3',1,1,now(), NULL, NULL);
INSERT INTO `ofds_group` 
	(`group_name`, `description`, `is_valid`, `created_by`, `created_date`, `modified_by`, `modified_date`) 
VALUES 
	('Grade 4','This is group for users in grade 4',1,1,now(), NULL, NULL);
/*!40000 ALTER TABLE `ofds_group` ENABLE KEYS */;
UNLOCK TABLES;
