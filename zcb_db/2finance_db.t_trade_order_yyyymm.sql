create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_trade_order_YYYYMM;
CREATE TABLE t_trade_order_YYYYMM(	
	FlistId		varchar(32)	NOT NULL COMMENT '交易单号',
	FbindId 	varchar(16)	NOT NULL COMMENT '绑定ID',
	Fspid		varchar(16) 	NOT NULL COMMENT '商户号',
	FbizCode	varchar(64)	NOT NULL COMMENT '业务代码',
	FoutTradeNo	varchar(32)	NOT NULL COMMENT '商户订单号',
	FtotalFee	decimal(13,2)	NOT NULL default 0 COMMENT '总金额',
	FuseType	int 		NOT NULL default 1 COMMENT '1： t+1日归还（默认都是t+1日）2： n天之后归还',
	FuseDays	int 		NOT NULL default 0 COMMENT '资金使用天数n',
	FmoneyType	int 		NOT NULL default 1 COMMENT '资金类型 1商户自有资金 2垫资平台资金',
	FchargeFee	decimal(13,2)	NOT NULL default 0 COMMENT '手续费',
	FchargeType	tinyint(4)	NOT NULL default 0 COMMENT '手续费收取方',
	FrateId		bigint(20)	NOT NULL default 0 COMMENT '如果charge_type=3 实时费率，这里记录费率表中的rate_id',
	FbankType	int(11)		NOT NULL default 0 COMMENT '接收资金的用户银行卡类型',
	FcardNo	varchar(32)	NOT NULL COMMENT '接收资金的用户银行卡号',
	FtrueName	varchar(32)	NOT NULL COMMENT '用户姓名（与银行卡一致）',
	Fstate		tinyint(4)	NOT NULL COMMENT '状态',
	FfetchLlistId	varchar(32)	NOT NULL COMMENT '给用户提现的交易单号',
	FtransferListId	varchar(255)	NOT NULL COMMENT '垫资转账单号',
	FfreezeListId	varchar(32)	NOT NULL COMMENT '冻结单号',
	FcreateTime   	datetime 	NOT NULL COMMENT '#创建时间',
	FmodifyTime    datetime 	NOT NULL COMMENT '#修改时间',
	Facc_time   	datetime	NOT NULL COMMENT '交易时间',
	Fsign		varchar(32)	NOT NULL COMMENT '签名字段,关键信息签名防止篡改',	
	Fmemo		varchar(128) 	NOT NULL COMMENT '备注',
	Fstandby1    	int(11)    	default NULL,
	Fstandby2    	int(11)    	default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(128) 	default NULL,
	Fstandby5    	datetime   	default NULL,
	Fstandby6    	datetime   	default NULL,
  PRIMARY KEY (`FlistId`),
  KEY `idx_spid` (`Fspid`,`FbizCode`,`FoutTradeNo`),
  KEY `idx_acctime` (`Facc_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
