create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_sp_config;
CREATE TABLE t_sp_config(	
	Fuid			bigint(20)		AUTO_INCREMENT 			COMMENT '自增主键 内部ID，1000000起增',
	Fspid			varchar(16) 	NOT NULL 				COMMENT '商户号',
	FspName			varchar(64)		NOT NULL 				COMMENT '商户名称',
	Flstate			tinyint(4)		NOT NULL default '1' 	COMMENT '物理状态: 1:有效 2:无效',	
	FauthFflag		Long			default NULL 			COMMENT '权限位，每位代表一种权限，权限种类待定',
	FsignKey		varchar(64)		NOT NULL 				COMMENT '商户签名key',
	FcreateTime 	datetime 		NOT NULL 				COMMENT '创建时间',
	FmodifyTime    	datetime 		NOT NULL				COMMENT '修改时间',
	Fmemo			varchar(128) 	NOT NULL 				COMMENT '备注',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(128) 	default NULL,
	Fstandby5    	datetime   		default NULL,
	Fstandby6    	datetime   		default NULL,
  PRIMARY KEY (`Fuid`),
  KEY `idx_spid` (`Fspid`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8;
