create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_money_rate;
CREATE TABLE t_money_rate(	
	FrateId			bigint(20)		AUTO_INCREMENT 			COMMENT '自增主键',
	FchargeType		int (16) 		NOT NULL 				COMMENT '1 按费率收费2 按笔收费3 按费率和笔收费',
    FchargeFee		decimal(13,2) 	default 0 				COMMENT '单笔收费金额（单位为分）',	
    FchargeRate		decimal(13,2) 	default 0 				COMMENT '费率',	
    Flstate			tinyint(4)		NOT NULL default '1' 	COMMENT '物理状态: 1:有效 2:无效',	
    FstartTime		datetime		NOT NULL 				COMMENT '使用资金的开始时间点 精确到秒',
    FendTime		datetime 		NOT NULL 				COMMENT '使用资金的开始时间点 精确到秒',
    FstartUseDays	int (11) 		NOT NULL 				COMMENT '资金使用最短时间',
    FendUseDays		int (11) 		NOT NULL 				COMMENT '资金使用最长时间',
    FstartMoney		bigint (20) 	default 0  				COMMENT '条件开始的借贷金额大小',
    FendMoney		tinyint (4) 	NOT NULL   				COMMENT '条件结束的借贷金额大小',
	Fmemo			varchar(128) 	NOT NULL 				COMMENT '备注',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(128) 	default NULL,
	Fstandby5    	datetime   		default NULL,
	Fstandby6    	datetime   		default NULL,
  PRIMARY KEY (`FrateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
