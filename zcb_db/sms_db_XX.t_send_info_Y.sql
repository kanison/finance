CREATE DATABASE IF NOT EXISTS `sms_db_$XX` DEFAULT CHARACTER SET utf8;

USE `sms_db_$XX`;

DROP TABLE IF EXISTS `t_send_info_$Y`;

CREATE TABLE `t_send_info_$Y` (
  `Fpkid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水编号（自增主键）',
  `Fmobile_no` varchar(32) NOT NULL COMMENT '手机号码',
  `Fclient_ip` varchar(32) NOT NULL COMMENT '用户IP',
  `Fsend_time` datetime NOT NULL COMMENT '发送时间',
  `Ftmpl_id` bigint(20) NOT NULL COMMENT '短信模板ID',
  PRIMARY KEY (`Fpkid`),
  KEY `IDX_NM_MOBILE_SEND_TIME` (`Fmobile_no`,`Fsend_time`),
  KEY `IDX_NM_CLIENT_IP_SEND_TIME` (`Fclient_ip`,`Fsend_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
