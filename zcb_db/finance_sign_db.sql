-- MySQL dump 10.10
--
-- Host: localhost    Database: finance_sign_db
-- ------------------------------------------------------
-- Server version	5.0.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database if not exists finance_sign_db;
use finance_sign_db;
--
-- Table structure for table `t_cft_reply_key`
--

DROP TABLE IF EXISTS `t_cft_reply_key`;
CREATE TABLE `t_cft_reply_key` (
  `Fsp_id` varchar(20) NOT NULL default '',
  `Fcft_reply_public_key` text NOT NULL,
  `Fcft_reply_private_key` text NOT NULL,
  `Fcreate_time` datetime NOT NULL default '0000-00-00 00:00:00',
  `Fmemo` text,
  PRIMARY KEY  (`Fsp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_cft_reply_key`
--


/*!40000 ALTER TABLE `t_cft_reply_key` DISABLE KEYS */;
LOCK TABLES `t_cft_reply_key` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `t_cft_reply_key` ENABLE KEYS */;

--
-- Table structure for table `t_cft_reply_rsa_key_change_log`
--

DROP TABLE IF EXISTS `t_cft_reply_rsa_key_change_log`;
CREATE TABLE `t_cft_reply_rsa_key_change_log` (
  `Fid` int(11) NOT NULL auto_increment,
  `Fsp_id` varchar(20) NOT NULL default '',
  `Fcft_reply_public_key` text NOT NULL,
  `Fcft_reply_private_key` text NOT NULL,
  `Fchange_time` datetime NOT NULL default '0000-00-00 00:00:00',
  `Fchange_reason` text NOT NULL,
  PRIMARY KEY  (`Fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_cft_reply_rsa_key_change_log`
--


/*!40000 ALTER TABLE `t_cft_reply_rsa_key_change_log` DISABLE KEYS */;
LOCK TABLES `t_cft_reply_rsa_key_change_log` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `t_cft_reply_rsa_key_change_log` ENABLE KEYS */;

--
-- Table structure for table `t_sp_md5key_config`
--

DROP TABLE IF EXISTS `t_sp_md5key_config`;
CREATE TABLE `t_sp_md5key_config` (
  `Fspid` varchar(16) NOT NULL,
  `Flstate` tinyint(4) NOT NULL default '1',
  `FsignKey` varchar(64) NOT NULL,
  `FcreateTime` datetime NOT NULL,
  `FmodifyTime` datetime NOT NULL,
  `FkeyIndex` int(11) NOT NULL,
  `Fmemo` varchar(128) default NULL,
  `Fstandby1` int(11) default NULL,
  `Fstandby4` varchar(128) default NULL,
  `Fstandby5` datetime default NULL,
  KEY `idx_spid` (`Fspid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_sp_md5key_config`
--


/*!40000 ALTER TABLE `t_sp_md5key_config` DISABLE KEYS */;
LOCK TABLES `t_sp_md5key_config` WRITE;
INSERT INTO `t_sp_md5key_config` VALUES ('2000000501',1,'e10adc3949ba59abbe56e057f20f883e','2015-04-21 15:36:54','2015-04-21 15:36:54',1,'test spid',NULL,NULL,NULL);
UNLOCK TABLES;
/*!40000 ALTER TABLE `t_sp_md5key_config` ENABLE KEYS */;

--
-- Table structure for table `t_sp_request_public_key_change_log`
--

DROP TABLE IF EXISTS `t_sp_request_public_key_change_log`;
CREATE TABLE `t_sp_request_public_key_change_log` (
  `Fid` int(11) NOT NULL auto_increment,
  `Fsp_id` varchar(20) NOT NULL default '',
  `Fsp_key_index` int(11) NOT NULL default '0',
  `Fsp_public_key` text NOT NULL,
  `Fchange_time` datetime NOT NULL default '0000-00-00 00:00:00',
  `Fchange_reason` text NOT NULL,
  PRIMARY KEY  (`Fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_sp_request_public_key_change_log`
--


/*!40000 ALTER TABLE `t_sp_request_public_key_change_log` DISABLE KEYS */;
LOCK TABLES `t_sp_request_public_key_change_log` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `t_sp_request_public_key_change_log` ENABLE KEYS */;

--
-- Table structure for table `t_sp_request_rsa_public_key`
--

DROP TABLE IF EXISTS `t_sp_request_rsa_public_key`;
CREATE TABLE `t_sp_request_rsa_public_key` (
  `Fsp_id` varchar(20) NOT NULL default '',
  `Fsp_key_index` int(11) NOT NULL default '0',
  `Fsp_public_key` text NOT NULL,
  `Fis_active` tinyint(4) NOT NULL default '1',
  `Fmemo` text,
  `Fcreate_time` datetime default NULL,
  `Fstandby1` int(11) default NULL,
  `Fstandby2` int(11) default NULL,
  `Fstandby3` varchar(128) default NULL,
  `Fstandby4` varchar(128) default NULL,
  `Fstandby5` datetime default NULL,
  `Fstandby6` datetime default NULL,
  PRIMARY KEY  (`Fsp_id`,`Fsp_key_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_sp_request_rsa_public_key`
--


/*!40000 ALTER TABLE `t_sp_request_rsa_public_key` DISABLE KEYS */;
LOCK TABLES `t_sp_request_rsa_public_key` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `t_sp_request_rsa_public_key` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

