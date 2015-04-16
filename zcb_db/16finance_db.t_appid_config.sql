create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_appid_config;
CREATE TABLE t_appid_config(	
	FappId			int		COMMENT '应用id',
	FstartNo 		int		COMMENT '开始自增序号',
  PRIMARY KEY (`FappId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
