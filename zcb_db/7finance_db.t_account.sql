create database if not exists finance_db;
use finance_db;

#DROP TABLE IF EXISTS t_account;
CREATE TABLE t__account(
	Facctid	bigint(20) AUTO_INCREMENT,
	Fuid	bigint(20) NOT NULL,
	FaccountType	smallint(6)	NOT NULL default '1' COMMENT '账户类型',
	FcurType	smallint(6)	NOT NULL default '156' COMMENT '币种',
	Fbalance	decimal(13,2)	NOT NULL default '0' COMMENT '账户余额',
	FfreezeBalance	decimal(13,2)	NOT NULL default '0' COMMENT '账户冻结金额',
	Fstate		smallint(6)	NOT NULL default '1',
	FcreateTime    datetime NOT NULL,
	FmodifyTime    datetime NOT NULL,
	Fsign	varchar(32) NOT NULL COMMENT '防串改签名',
	Fmemo        varchar(128) default NULL,
	Fstandby1    int(11)    default NULL,
	Fstandby2    int(11)    default NULL,
	Fstandby3    varchar(64) default NULL,
	Fstandby4    varchar(128) default NULL,
	Fstandby5    datetime   default NULL,
	Fstandby6    datetime   default NULL,
  PRIMARY KEY (`Facctid`),
  KEY `idx_uid` (`Fuid`,`FaccountType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
