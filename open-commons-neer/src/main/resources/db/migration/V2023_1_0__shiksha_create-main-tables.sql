CREATE TABLE `shiksha_evaluation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(127) NOT NULL,
  `description` VARCHAR(256) NULL,
  `eval_group` VARCHAR(50) NULL,
  `eval_env` VARCHAR(20) NULL,
  `questions_in_eval` INT NULL,
  `questions_to_attempt` INT NULL,
  `questions_to_pass` INT NULL,
  `show_report` tinyint(1) DEFAULT '0',
  `is_valid` tinyint(1) DEFAULT '1',
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` bigint DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `shiksha_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `eval_id` BIGINT NULL,
  `question_group` VARCHAR(450) NULL,
  `question_text` VARCHAR(4500) NULL,
  `question_type` VARCHAR(15) NULL,
  `image_name` VARCHAR(500) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `shiksha_question` 
ADD CONSTRAINT `fk_shiksha_quest_eval_id`
  FOREIGN KEY (`eval_id`)
  REFERENCES `shiksha_evaluation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `shiksha_answer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `question_id` BIGINT NULL,
  `answer_text` VARCHAR(450) NULL,
  `correct_option` BIT(1) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `shiksha_answer` 
ADD CONSTRAINT `fk_shiksha_question_id`
    FOREIGN KEY (`question_id`)
    REFERENCES `shiksha_question` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

CREATE TABLE `shiksha_eval_stats` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NULL,
  `eval_id` BIGINT NULL,
  `eval_start_date_time` DATETIME NULL,
  `eval_end_date_time` DATETIME NULL,
  `eval_stat_passed` TINYINT(1) NULL,
  `eval_stat_status` VARCHAR(45) NULL,
  `eval_stat_result` VARCHAR(45) NULL,
  `correct_answer_count` INT NULL,
  `quest_attempt_count` INT NULL,
  `eval_quest_count` INT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `shiksha_eval_stats` 
ADD INDEX `fk_shiksha_user_id_idx` (`user_id` ASC) VISIBLE;

ALTER TABLE `shiksha_eval_stats` 
ADD CONSTRAINT `fk_shiksha_evalstats_user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `ofds_user` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `shiksha_eval_stats` 
ADD INDEX `fk_shiksha_eval_id_idx` (`eval_id` ASC) VISIBLE;

ALTER TABLE `shiksha_eval_stats` 
ADD CONSTRAINT `fk_shiksha_evalstats_eval_id`
  FOREIGN KEY (`eval_id`)
  REFERENCES `shiksha_evaluation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

