create database if not exists finance_db;
use finance_db;

CREATE TABLE t_sp_info(
	Fuid	bigint(20) AUTO_INCREMENT,
	Fspid varchar(16) NOT NULL,
	Fsp_name varchar(64) NOT NULL,	
	Fstate	smallint(6) NOT NULL default 1,
	Flstate	smallint(6) NOT NULL default 1,	
	Fkey varchar(64) NOT NULL default '',
	Fauthflag bigint NOT NULL default '65535',
	Fcreate_time    datetime NOT NULL,
	Fmodify_time    datetime NOT NULL,
	Fmemo        varchar(255) default NULL,
	Fsign	varchar(32) NOT NULL default '' COMMENT '防串改签名',
	Fstandby1    int(11)    default NULL,
	Fstandby2    int(11)    default NULL,
	Fstandby3    bigint(20)    default NULL,
	Fstandby4    decimal(18,2) NOT NULL default '0',
	Fstandby5    datetime   default NULL,
	Fstandby6    varchar(64) default NULL,
	Fstandby7    varchar(128) default NULL,
	Fstandby8    varchar(255) default NULL,
  PRIMARY KEY (`Fuid`),
  UNIQUE KEY `idx_spid` (`Fspid`),
  KEY `idx_mtime` (`Fmodify_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8;
