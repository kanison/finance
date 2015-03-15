create database if not exists finance_db;
use finance_db;

#DROP TABLE IF EXISTS t_acctroll_list_YYYYMM;
CREATE TABLE t_acctroll_list_YYYYMM(
	Fbkid	bigint(20) AUTO_INCREMENT,
	FlistId varchar(32) NOT NULL,
	FfreezeId varchar(32),
	Fcoding varchar(64),
	Facctid	bigint(20) NOT NULL COMMENT '资金账户id',
	Fuid	bigint(20) NOT NULL COMMENT '用户id',
	FaccountType	smallint(6)	NOT NULL default '1' COMMENT '账户类型',
	FcurType	smallint(6)	NOT NULL default '156' COMMENT '币种',
	FpayNum		decimal(13,2)	NOT NULL default '0' COMMENT '本次交易变更金额',
	FfreezeNum	decimal(13,2)	NOT NULL default '0' COMMENT '本次交易变更冻结金额',
	Fbalance	decimal(13,2)	NOT NULL default '0' COMMENT '账户余额',
	FfreezeBalance	decimal(13,2)	NOT NULL default '0' COMMENT '账户冻结金额',
	Fsubject	smallint(6) 	NOT NULL COMMENT '具体业务子类型',
	Ftype 		tinyint(4)	NOT NULL COMMENT '操作类型',
	FtradeTime    varchar(21) NOT NULL COMMENT '交易时间',
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
  PRIMARY KEY (`Fbkid`),
  KEY `idx_uid` (`Fuid`),
  KEY `idx_listid` (`FlistId`),
  KEY `idx_tradeTime` (`FtradeTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
