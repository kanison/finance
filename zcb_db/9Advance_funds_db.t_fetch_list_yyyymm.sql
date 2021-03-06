create database if not exists advance_funds_db;
use advance_funds_db;

DROP TABLE IF EXISTS t_fetch_list_YYYYMM;
CREATE TABLE t_fetch_list_YYYYMM(	
	FfetchListId	varchar(32) 	NOT NULL		COMMENT '垫资系统提现单号',
    FlistId    		varchar(32) 	NOT NULL		COMMENT '交易订单号',
    Fstate			smallint		NOT NULL 		COMMENT '记录状态',	    
    Fnum 			decimal(13,2) 	default 0 		COMMENT '金额',	
    FcurType		smallint		NOT NULL 		COMMENT '币种类型',	 
    FcardName	    varchar(32) 	NOT NULL		COMMENT '银行卡账户名',
    FcardId	    	varchar(32) 	NOT NULL		COMMENT '银行卡卡号(加密)',
    FuserType		smallint		NOT NULL 		COMMENT '1 个人 2 公司',	    
    FbankType		smallint		NOT NULL 		COMMENT '银行类型',	    
    FbankName	    varchar(64) 	NOT NULL		COMMENT '开户行名',
    FbankArea	    varchar(16) 	NOT NULL		COMMENT '开户行省份',
    FbankCity	    varchar(16) 	NOT NULL		COMMENT '开户行地区',
    Fmemo			varchar(128) 	NOT NULL 		COMMENT '后台操作memo',
    FfetchMemo		varchar(128) 	NOT NULL 		COMMENT '银行提现返回信息（主要是记录错误信息）',
    FcreateTime		datetime		NOT NULL 		COMMENT '记录创建时间',
    FfetchFrontTime	datetime		NOT NULL 		COMMENT '提现发起时间',
    FfetchTime		datetime		NOT NULL 		COMMENT '提现结果回导时间',
    FmodifyTime		datetime		NOT NULL 		COMMENT '最后修改时间',
    FreturnTime		datetime		NOT NULL 		COMMENT '退票时间',
    Fsign			varchar(32) 	NOT NULL 		COMMENT '关键字段签名',
    Fnotify	    	int    			NOT NULL	 	COMMENT '提现通知用户',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(64) 	default NULL,
    Fstandby5    	varchar(255) 	default NULL,
    Fstandby6    	varchar(255) 	default NULL,
    Fstandby7    	varchar(255) 	default NULL,    
	Fstandby8    	datetime   		default NULL,
	Fstandby9    	datetime   		default NULL,
  PRIMARY KEY (`FfetchListId`),
  KEY `idx_createtime` (`FcreateTime`),
  KEY `idx_modifytime` (`FmodifyTime`),
  KEY `idx_listid` (`Flistid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
