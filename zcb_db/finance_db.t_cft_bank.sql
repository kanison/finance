create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_cft_bank;
CREATE TABLE t_cft_bank(	
	FbankType			varchar(20)		COMMENT '银行类型',
	FbankCode	 		varchar(4)		COMMENT '银行编码',
    FareaRequest_pub		char			COMMENT '对公验证省份城市 Y验证 N不验证',	
	FareaRequest_priv		char			COMMENT '对私验证省份城市 Y验证 N不验证',	
    FsubbankRequest_pub		char			COMMENT '对公验证支行信息 Y验证 N不验证',	
    FsubbankRequest_priv	char			COMMENT '对私验证支行信息 Y验证 N不验证',	
    FbankState			char			COMMENT '银行状态 1有效 0失效',	
  PRIMARY KEY (`FbankCode`),
  UNIQUE KEY (`FbankType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_cft_bank(FbankType,FbankCode,FareaRequest_pub,FareaRequest_priv,FsubbankRequest_pub,FsubbankRequest_priv,FbankState) 
values
('招商银行','1001','Y','N','Y','N',1),
('中国工商银行','1002','N','N','N','N',1),
('中国建设银行','1003','Y','N','Y','N',1),
('上海浦东发展银行','1004','Y','N','Y','N',1),
('中国农业银行','1005','Y','N','Y','N',1),
('兴业银行','1009','N','N','N','N',1),
('北京银行','1032','N','N','N','N',1),
('中国光大银行','1022','Y','N','Y','N',1),
('中国民生银行','1006','Y','N','Y','N',1),
('中信实业银行','1021','Y','N','Y','N',1),
('广东发展银行','1027','N','N','N','N',1),
('平安银行(含深发展)','1010','N','N','N','N',1),
('中国银行','1026','Y','Y','Y','Y',1),
('中国交通银行','1020','Y','N','N','N',1),
('中国邮政储蓄银行/邮政储汇','1066','N','N','N','N',1),
('其他银行','1099','Y','Y','Y','Y',1);