create database if not exists finance_db;
use finance_db;

DROP TABLE IF EXISTS t_cft_area;
CREATE TABLE t_cft_area(	
	FareaName			varchar(20)		COMMENT '地区名称',
	FareaCode	 		varchar(2)		COMMENT '地区编码',
    FareaState			char			COMMENT '地区状态 1有效 0失效',
  UNIQUE KEY (`FareaCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_cft_area values
('北京','1',1),
('上海','2',1),
('天津','3',1),
('重庆','4',1),
('河北','5',1),
('山西','6',1),
('内蒙古','7',1),
('辽宁','8',1),
('吉林','9',1),
('黑龙江','10',1),
('江苏','11',1),
('浙江','12',1),
('安徽','13',1),
('福建','14',1),
('江西','15',1),
('山东','16',1),
('河南','17',1),
('湖北','18',1),
('湖南','19',1),
('广东','20',1),
('广西','21',1),
('海南','22',1),
('四川','23',1),
('贵州','24',1),
('云南','25',1),
('西藏','26',1),
('陕西','27',1),
('甘肃','28',1),
('宁夏','29',1),
('青海','30',1),
('新疆','31',1);