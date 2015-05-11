CREATE DATABASE IF NOT EXISTS `sms_db` DEFAULT CHARACTER SET utf8;

USE `sms_db`;

DROP TABLE IF EXISTS `t_mobile_limit`;

CREATE TABLE `t_mobile_limit` (
  `Fpkid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水编号（自增主键）',
  `Fmobile_no` varchar(32) NOT NULL COMMENT '手机号码',
  `Ftmpl_id` bigint(20) NOT NULL COMMENT '短信模板ID，触发受限的短信模板ID',
  `Fblock_stime` datetime DEFAULT NULL COMMENT '受限制的开始时间',
  `Fblock_timespan` int(11) DEFAULT NULL COMMENT '受限制的时长，秒为单位，只有当前时间大于Fblock_stime+ Fblock_timespan才解除限制',
  PRIMARY KEY (`Fpkid`),
  UNIQUE KEY `IDX_UNIQ_MOBILE_NO` (`Fmobile_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
