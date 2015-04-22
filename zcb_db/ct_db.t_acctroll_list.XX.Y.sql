﻿create database if not exists ct_db_$XX;
use ct_db_$XX;

CREATE TABLE t_acctroll_list_$Y(
	Fpkid	bigint(20) AUTO_INCREMENT,
	Flistid varchar(64) NOT NULL,
	Facctid	bigint(20) NOT NULL COMMENT '资金账户id',
	Fuid	bigint(20) NOT NULL COMMENT '用户id',
	Fuserid varchar(64) default NULL COMMENT '用户名',
	Facct_type	smallint(6)	NOT NULL COMMENT '账户类型',
	Fcur_type	smallint(6)	NOT NULL COMMENT '币种',
	Fto_uid	bigint(20) NOT NULL COMMENT '用户id',
	Fto_userid varchar(64) default NULL COMMENT '用户名',
	Fpay_amt		decimal(18,2)	NOT NULL default '0' COMMENT '本次交金额',
	Fpayfreeze_amt	decimal(18,2)	NOT NULL default '0' COMMENT '本次交冻结金额',
	Fbalance	decimal(18,2)	NOT NULL default '0' COMMENT '账户余额',
	Ffreeze_balance	decimal(18,2)	NOT NULL default '0' COMMENT '账户冻结金额',
	Faction_type	smallint(6) 	NOT NULL COMMENT '操作类型',
	Ftrans_type smallint(6) 	NOT NULL COMMENT '交易类型',
	Ftype 		tinyint(4)	NOT NULL COMMENT '记账类型',
	Ftrade_acc_time datetime NOT NULL,
	Fcreate_time    datetime NOT NULL,
	Fmodify_time    datetime NOT NULL,
	Fsign	varchar(32) NOT NULL default '' COMMENT '防串改签名',
	Fmemo        varchar(255) default NULL,
	Fstandby1    int(11)    default NULL,
	Fstandby2    int(11)    default NULL,
	Fstandby3    varchar(64) default NULL,
	Fstandby4    varchar(128) default NULL,
	Fstandby5    datetime   default NULL,
	Fstandby6    varchar(255) default NULL,
	Fstandby7    decimal(18,2) default '0',
  PRIMARY KEY (`Fpkid`),
  KEY `idx_uid` (`Fuid`),
  KEY `idx_listid` (`Flistid`),
  KEY `idx_tradetime` (`Ftrade_acc_time`),
  KEY `idx_mtime` (`Fmodify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;