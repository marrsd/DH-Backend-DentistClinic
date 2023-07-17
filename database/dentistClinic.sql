CREATE DATABASE  IF NOT EXISTS `dentist_clinic`;
USE `dentist_clinic`;


DROP TABLE IF EXISTS `addresses`;
CREATE TABLE `addresses` (
  `address_id` bigint NOT NULL AUTO_INCREMENT,
  `locality` varchar(255) DEFAULT NULL,
  `number` int NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`address_id`)
)

DROP TABLE IF EXISTS `dentist`;
CREATE TABLE `dentist` (
  `dentist_id` bigint NOT NULL AUTO_INCREMENT,
  `dni` bigint NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `registration_number` bigint NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`dentist_id`),
  UNIQUE KEY `UK_m5kc485tp9iles2yawtfhaglw` (`dni`),
  UNIQUE KEY `UK_blkbder653fpmbt9tt1ng384r` (`registration_number`),
  KEY `FK9bqls44d0uqrt5uy5xemny1yq` (`id_user`),
  CONSTRAINT `FK9bqls44d0uqrt5uy5xemny1yq` FOREIGN KEY (`id_user`) REFERENCES `users` (`user_id`)
) 

DROP TABLE IF EXISTS `patients`;
CREATE TABLE `patients` (
  `patient_id` bigint NOT NULL AUTO_INCREMENT,
  `date_hour_admission` datetime(6) DEFAULT NULL,
  `dni` bigint NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `id_address` bigint NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `UK_kwg4jhsdnxhullscn35csrnce` (`dni`),
  KEY `FKthaivh3yvegad4456gslpfi32` (`id_address`),
  KEY `FK6jy7jj1akakjuqs53ovilsdad` (`id_user`),
  CONSTRAINT `FK6jy7jj1akakjuqs53ovilsdad` FOREIGN KEY (`id_user`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKthaivh3yvegad4456gslpfi32` FOREIGN KEY (`id_address`) REFERENCES `addresses` (`address_id`)
) 

DROP TABLE IF EXISTS `turns`;
CREATE TABLE `turns` (
  `turn_id` bigint NOT NULL AUTO_INCREMENT,
  `date_hour` datetime(6) DEFAULT NULL,
  `id_dentist` bigint NOT NULL,
  `id_patient` bigint NOT NULL,
  PRIMARY KEY (`turn_id`),
  KEY `FK93vi4aapd38js729yap8hawju` (`id_dentist`),
  KEY `FKlai2gvds7m2f1o2wjrf8kbrk6` (`id_patient`),
  CONSTRAINT `FK93vi4aapd38js729yap8hawju` FOREIGN KEY (`id_dentist`) REFERENCES `dentist` (`dentist_id`),
  CONSTRAINT `FKlai2gvds7m2f1o2wjrf8kbrk6` FOREIGN KEY (`id_patient`) REFERENCES `patients` (`patient_id`)
)

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `asigned` bit(1) DEFAULT NULL,
  `is_admin` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) 