create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_gen_serialno_conf;
CREATE TABLE t_gen_serialno_conf(	
	Fbiz_id			int	 			NOT NULL			COMMENT '业务类型',
    Fdate   		datetime		NOT NULL 			COMMENT '日期',
    Fnumber   		long			NOT NULL 			COMMENT '当天预申请到的id',
    Finterval		int	 			NOT NULL			COMMENT '间隔',
    FcreateTime   	datetime 		NOT NULL 			COMMENT '创建时间',
	FmodifyTime    	datetime 		NOT NULL 			COMMENT '修改时间',
	Fmemo			varchar(128) 	NOT NULL 			COMMENT '备注',
	Fstandby1    	int(11)    		default NULL,
	Fstandby2    	int(11)    		default NULL,
	Fstandby3    	varchar(64) 	default NULL,
	Fstandby4    	varchar(64) 	default NULL,
    Fstandby5    	varchar(255) 	default NULL,
    Fstandby6    	varchar(255) 	default NULL,
    Fstandby7    	varchar(255) 	default NULL,    
	Fstandby8    	datetime   		default NULL,
	Fstandby9    	datetime   		default NULL,
  PRIMARY KEY (`Fbiz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
