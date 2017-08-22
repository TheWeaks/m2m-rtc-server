# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 192.168.22.145 (MySQL 5.5.57-0ubuntu0.14.04.1)
# Database: WeaksCall
# Generation Time: 2017-08-21 02:10:30 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

# Dump of table File
# ------------------------------------------------------------

DROP TABLE IF EXISTS `File`;

CREATE TABLE `File` (
  `fid`   VARCHAR(64)  NOT NULL DEFAULT '',
  `uid`   VARCHAR(64)  NOT NULL DEFAULT '',
  `rid`   VARCHAR(64)           DEFAULT NULL,
  `ftype` INT(11)               DEFAULT NULL,
  `fname` VARCHAR(256) NOT NULL DEFAULT '',
  `url`   VARCHAR(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`fid`),
  KEY `Fuid` (`uid`),
  KEY `Frid` (`rid`),
  CONSTRAINT `Frid` FOREIGN KEY (`rid`) REFERENCES `Room` (`rid`)
    ON UPDATE CASCADE,
  CONSTRAINT `Fuid` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# Dump of table History
# ------------------------------------------------------------

DROP TABLE IF EXISTS `History`;

CREATE TABLE `History` (
  `id`      INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rid`     VARCHAR(64)      NOT NULL DEFAULT '',
  `uid`     VARCHAR(64)               DEFAULT '',
  `date`    VARCHAR(64)      NOT NULL DEFAULT '',
  `htype`   INT(11)                   DEFAULT NULL,
  `message` VARCHAR(1024)             DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `Hrid` (`rid`),
  KEY `Huid` (`uid`),
  CONSTRAINT `Huid` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
    ON UPDATE CASCADE,
  CONSTRAINT `Hrid` FOREIGN KEY (`rid`) REFERENCES `Room` (`rid`)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# Dump of table Room
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Room`;

CREATE TABLE `Room` (
  `rid`      VARCHAR(64) NOT NULL DEFAULT '',
  `ordernum` VARCHAR(64)          DEFAULT NULL,
  `date`     DATE        NOT NULL,
  `rstate`   INT(11)              DEFAULT NULL,
  PRIMARY KEY (`rid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `Room` WRITE;
/*!40000 ALTER TABLE `Room`
  DISABLE KEYS */;

INSERT INTO `Room` (`rid`, `ordernum`, `date`, `rstate`)
VALUES
  ('1', '343caf5e-b702-4d60-9f2a-4f8c442bee53', '0000-00-00', NULL);

/*!40000 ALTER TABLE `Room`
  ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table RoomMember
# ------------------------------------------------------------

DROP TABLE IF EXISTS `RoomMember`;

CREATE TABLE `RoomMember` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rid`      VARCHAR(64)      NOT NULL DEFAULT '',
  `uid`      VARCHAR(64)      NOT NULL DEFAULT '',
  `jointime` DATE             NOT NULL,
  `rmstate`  INT(11)                   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `RMrid` (`rid`),
  KEY `RMuid` (`uid`),
  CONSTRAINT `RMuid` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
    ON UPDATE CASCADE,
  CONSTRAINT `RMrid` FOREIGN KEY (`rid`) REFERENCES `Room` (`rid`)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `RoomMember` WRITE;
/*!40000 ALTER TABLE `RoomMember`
  DISABLE KEYS */;

INSERT INTO `RoomMember` (`id`, `rid`, `uid`, `jointime`, `rmstate`)
VALUES
  (1, '1', '1', '0000-00-00', NULL);

/*!40000 ALTER TABLE `RoomMember`
  ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `uid`    VARCHAR(64) NOT NULL DEFAULT '',
  `name`   VARCHAR(64) NOT NULL DEFAULT '',
  `prefix` VARCHAR(64)          DEFAULT NULL,
  `suffix` VARCHAR(64)          DEFAULT NULL,
  PRIMARY KEY (`uid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User`
  DISABLE KEYS */;

INSERT INTO `User` (`uid`, `name`, `prefix`, `suffix`)
VALUES
  ('1', 'name', NULL, NULL);

/*!40000 ALTER TABLE `User`
  ENABLE KEYS */;
UNLOCK TABLES;


/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
