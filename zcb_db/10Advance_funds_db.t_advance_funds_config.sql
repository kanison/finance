create database if not exists finance_db;
use advance_funds_db;

DROP TABLE IF EXISTS t_advance_funds_config;
CREATE TABLE t_advance_funds_config(	
	FkeyWord		varchar(128) 	NOT NULL		COMMENT 'key',
    Fvalue    		text		 	NOT NULL		COMMENT 'value',
    FcreateTime		datetime		NOT NULL 		COMMENT '记录创建时间',
	FmodifyTime		datetime		NOT NULL 		COMMENT '最后修改时间',
    Fmemo			varchar(128) 	NOT NULL 		COMMENT 'memo',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(64) 	default NULL,
    Fstandby5    	varchar(255) 	default NULL,
    Fstandby6    	varchar(255) 	default NULL,
    Fstandby7    	varchar(255) 	default NULL,    
	Fstandby8    	datetime   		default NULL,
	Fstandby9    	datetime   		default NULL,
  PRIMARY KEY (`FkeyWord`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
