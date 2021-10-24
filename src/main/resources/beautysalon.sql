-- DROP TABLE service;
-- DROP TABLE `order`;
-- DROP DATABASE `beautysalon`;
CREATE DATABASE `beautysalon`;
USE `beautysalon`;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` ENUM('admin', 'employee', 'client') NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL UNIQUE,
  `password` VARCHAR(45) NOT NULL,
  `role_id` INT NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`));

CREATE TABLE IF NOT EXISTS `admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`));

CREATE TABLE IF NOT EXISTS `client` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`));

CREATE TABLE IF NOT EXISTS `profession` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `employee` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `rating` DOUBLE NOT NULL,
  `profession_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  FOREIGN KEY (`profession_id`) REFERENCES `profession` (`id`));

CREATE TABLE IF NOT EXISTS `service` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DOUBLE NOT NULL,
  `spend_time` TIME NOT NULL,
  `profession_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`profession_id`) REFERENCES `profession` (`id`));

CREATE TABLE IF NOT EXISTS `ordering` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ordering_date_time` DATETIME NOT NULL,
  `service_id` INT NOT NULL,
  `employee_id` INT NOT NULL,
  `client_id` INT NOT NULL,
  `status` ENUM('active', 'done') NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  FOREIGN KEY (`client_id`) REFERENCES `client` (`id`));


INSERT `role`(name) VALUES('admin');
INSERT `role`(name) VALUES('employee');
INSERT `role`(name) VALUES('client');
INSERT `account`(login, password, role_id) VALUE('Dave', 12345, 1);
INSERT `account`(login, password, role_id) VALUE('Mary23', 1945, 1);
INSERT `account`(login, password, role_id) VALUE('Ivan45', 345, 2);
INSERT `account`(login, password, role_id) VALUE('Vovan', 15, 3);
INSERT `account`(login, password, role_id) VALUE('Irina26', 1585, 3);
INSERT `account`(login, password, role_id) VALUE('Veronika', 1695, 2);
INSERT `account`(login, password, role_id) VALUE('Mika', 1656, 2);
INSERT `account`(login, password, role_id) VALUE('Vera26', 1345, (SELECT `id` FROM `role` WHERE `name` = "client"));
INSERT `admin`(account_id, name, surname) VALUE(1, 'Дмитрий', 'Пупков');
INSERT `profession`(name) VALUES("Мастер маникюра"), ("Визажист"), ("Массажист");
INSERT `employee`(account_id, name, surname, profession_id, rating) VALUE(2, 'Иван', 'Петров', 1, 0);
INSERT `employee`(account_id, name, surname, profession_id, rating) VALUE(4, 'Вероника', 'Гиппиус', 1, 0);
INSERT `service`(name, price, spend_time, profession_id) VALUE('Маникюр', 200, "00:30:00", 1);
INSERT `service`(name, price, spend_time, profession_id) VALUE('Стрижка', 400, "01:00:00", (SELECT `id` FROM `profession` WHERE `name` = "Визажист"));
-- UPDATE `service` SET `spend_time` = "00:30:00" WHERE `name` = "Маникюр";
-- UPDATE `service` SET `spend_time` = "01:00:00" WHERE `name` = "Стрижка";
INSERT `client`(account_id, name, surname) VALUE(3, 'Владимир', 'Кличко');
INSERT `client`(account_id, name, surname) VALUE((SELECT `id` FROM `account` WHERE `login` = "Vera26"), 'Вероника', 'Кудина');
INSERT `ordering`(`ordering_date_time`, `service_id`, `employee_id`, `client_id`, `status`)
VALUE("2021-11-05 13:00:00", 1, 1, 1, "active");
SELECT * FROM `account`;
SELECT * FROM `admin`;
SELECT * FROM `client`;
SELECT * FROM `profession`;
SELECT * FROM `service`;
SELECT * FROM `order`;
SELECT * FROM `role`;
SELECT * FROM `order`;
SELECT ordering_date_time, status, service_id, employee_id, client_id, create_time, update_time, 
service.name, service.price, service.spend_time, 
employee.name, employee.surname, employee.rating, 
employee.profession_id, 
profession.name, 
client.name, client.surname 
FROM ordering JOIN service ON service.id = ordering.service_id 
JOIN employee ON employee.id = ordering.employee_id JOIN profession ON profession.id = employee.profession_id 
JOIN client ON client.id = ordering.client_id 
WHERE ordering.id = 1;