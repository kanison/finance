create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_sp_biz_config;
CREATE TABLE t_sp_biz_config(	
	Fspid	    	varchar(16) 	NOT NULL				COMMENT '商户号',
    FbizCode    	varchar(64) 	NOT NULL				COMMENT '业务代码',
    Flstate			tinyint(4)		NOT NULL default '1' 	COMMENT '物理状态: 1:有效 2:无效',	    
	FadvanceId		int				NOT NULL	 			COMMENT '垫资类型id',
    FcreateTime		datetime		NOT NULL 				COMMENT '创建时间',
    FmodifyTime		datetime 		NOT NULL 				COMMENT '修改时间',
    Ftype	    	int(11)    		NOT NULL	 			COMMENT '业务类型',
    FrateId	    	bigint(20)   	NOT NULL	 			COMMENT '费率',
	FchargeRate 	decimal(13,2) 	default 0 				COMMENT '',	
    FchargeFee		decimal(13,2) 	default 0 				COMMENT '',	
    FchargeType	    tinyint(4) 		NOT NULL	 			COMMENT '',
    FbankType	    int(11)    		NOT NULL	 			COMMENT '垫资回补转到的垫资平台银行类型',
    FbankName    	varchar(64) 	NOT NULL				COMMENT '银行名称',
    FcardNo    		varchar(32) 	NOT NULL				COMMENT '垫资回补转到的垫资平台银行卡号',
    FtrueName    	varchar(32) 	NOT NULL				COMMENT '该银行卡持有人真实姓名',
	Fmemo			varchar(128) 	NOT NULL 				COMMENT '备注',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(128) 	default NULL,
	Fstandby5    	datetime   		default NULL,
	Fstandby6    	datetime   		default NULL,
  PRIMARY KEY (`Fspid`,`FbizCode`),
  KEY `idx_advanceid` (`FadvanceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
