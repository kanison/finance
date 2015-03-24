create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_user_bind;
CREATE TABLE t_user_bind(	
	Fpkid			bigint(20)		AUTO_INCREMENT 			COMMENT '自增序列,以100000000开始',
	FbindId 		varchar(16)		NOT NULL 				COMMENT '内部ID',
	Fspid			varchar(16) 	NOT NULL 				COMMENT '商户号',
	FspUserId		varchar(64)		NOT NULL 				COMMENT '用户在商户的账号',
	Flstate			tinyint(4)		NOT NULL default '1' 	COMMENT '物理状态: 1:有效 2:无效 3:冻结',	
	FcreType		tinyint(4)		default NULL 			COMMENT '证件类型',
	FcreId			varchar(32)		NOT NULL 				COMMENT '证件号',
	FtrueName		varchar(64) 	NOT NULL 				COMMENT '投资人真实姓名',
	Fphone			varchar(21) 	NOT NULL 				COMMENT '固定电话固定电话',
	Fmobile			varchar(21) 	NOT NULL 				COMMENT '手机号码',
	Facc_time   	datetime		NOT NULL 				COMMENT '开户时间',
	FcreateTime   	datetime 		NOT NULL 				COMMENT '创建时间',
	FmodifyTime   	datetime 		NOT NULL 				COMMENT '修改时间',
	Fmemo			varchar(128) 	NOT NULL 				COMMENT '备注',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(128) 	default NULL,
	Fstandby5    	datetime   		default NULL,
	Fstandby6    	datetime   		default NULL,
  PRIMARY KEY (`Fpkid`),
  KEY `idx_bindid` (`FbindId`),
  KEY `idx_spuserid` (`Fspid`,`FspUserId`),
  KEY `idx_acctime` (`Facc_time`),
  KEY `idx_creid` (`FcreId`),
  KEY `idx_modifytime` (`FmodifyTime`)
) ENGINE=InnoDB AUTO_INCREMENT=100000000 DEFAULT CHARSET=utf8;
