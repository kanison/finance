create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_cft_city;
CREATE TABLE t_cft_city(	
	FcityName			varchar(20)		COMMENT '城市名称',
	FareaCode	 		varchar(2)		COMMENT '地区编码',
    FcityCode	 		varchar(4)		COMMENT '城市编码',
    FareaState			char			COMMENT '城市状态 1有效 0失效',
  UNIQUE KEY (`FareaCode`,`FcityCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_cft_city values
('北京','1','10',1),
('上海','2','21',1),
('天津','3','22',1),
('重庆','4','23',1),
('涪陵市','4','230',1),
('万州市','4','231',1),
('黔江市','4','232',1)