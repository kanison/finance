create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_advance_type_config;
CREATE TABLE t_advance_type_config(	
	FadvanceId			bigint(20)		AUTO_INCREMENT 			COMMENT '自增主键',
    Fspid	    		varchar(16) 	NOT NULL				COMMENT '商户号',
    Flstate				tinyint(4)		NOT NULL default '1' 	COMMENT '物理状态: 1:有效 2:无效',		
    FspTotalQuoto		decimal(13,2) 	default 0 				COMMENT '商户自有资金额度',	
    FusedSpQuoto		decimal(13,2) 	default 0 				COMMENT '商户自有资金使用额度',	
    FcreditQuoto		decimal(13,2) 	default 0 				COMMENT '授信额度',	
    FusedCreditQuoto	decimal(13,2) 	default 0 				COMMENT '已用授信额度',	
    FcreditQuotoOffset	decimal(13,2) 	default 0 				COMMENT '可用额度阀值',	    
    FcreateTime			datetime		NOT NULL 				COMMENT '创建时间',
    FmodifyTime			datetime 		NOT NULL 				COMMENT '修改时间',
	Fmemo				varchar(128) 	NOT NULL 				COMMENT '备注',
	Fstandby1    		int(11)    		default NULL,
	Fstandby2    		int(11)    		default NULL,
	Fstandby3    		varchar(64) 	default NULL,
	Fstandby4    		varchar(128) 	default NULL,
	Fstandby5    		datetime   		default NULL,
	Fstandby6    		datetime   		default NULL,
  PRIMARY KEY (`FadvanceId`),
  KEY `idx_spid` (`Fspid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
