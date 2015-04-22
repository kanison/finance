create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_appid_config;
CREATE TABLE t_appid_config(	
	FappId			varchar(10)		COMMENT '应用id',
	FstartNo 		int				COMMENT '开始自增序号',
  PRIMARY KEY (`FappId`),
  UNIQUE KEY (`FstartNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
