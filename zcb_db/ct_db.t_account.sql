create database if not exists ct_db;
use ct_db;

CREATE TABLE t_account(
	Facctid	bigint(20) AUTO_INCREMENT,
	Fuid	bigint(20) NOT NULL,
	Fuserid	varchar(64) NOT NULL,
	Facct_type	smallint(6)	NOT NULL COMMENT '账户类型',
	Fcur_type	smallint(6)	NOT NULL default '156' COMMENT '币种',
	Fbalance	decimal(13,2)	NOT NULL default '0' COMMENT '账户余额',
	Ffreeze_balance	decimal(18,2)	NOT NULL default '0' COMMENT '账户冻结金额',
	Frecon_balance	decimal(18,2)	NOT NULL default '0' COMMENT '对账余额',
	Frecon_day varchar(20) NOT NULL,
	Fstate		smallint(6)	NOT NULL default '1',
	Flstate		smallint(6)	NOT NULL default '1',
	Fcreate_time    datetime NOT NULL,
	Fmodify_time    datetime NOT NULL,
	Fsign	varchar(32) NOT NULL COMMENT '防串改签名',
	Fmemo        varchar(128) default NULL,
	Fstandby1    int(11)    default NULL,
	Fstandby2    int(11)    default NULL,
	Fstandby3    varchar(64) default NULL,
	Fstandby4    varchar(128) default NULL,
	Fstandby5    datetime   default NULL,
	Fstandby6    varchar(255)   default NULL,
	Fstandby7    decimal(18,2) default NULL,
	Fstandby8    bigint default NULL,
  PRIMARY KEY (`Facctid`),
  UNIQUE KEY `idx_uid` (`Fuid`,`Facct_type`,`Fcur_type`),
  KEY `idx_mtime` (`Fmodify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
