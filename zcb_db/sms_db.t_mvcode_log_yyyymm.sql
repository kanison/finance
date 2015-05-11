CREATE DATABASE IF NOT EXISTS `sms_db` DEFAULT CHARACTER SET utf8;

USE `sms_db`

DROP TABLE IF EXISTS `t_mvcode_log_yyyymm`;

CREATE TABLE `t_mvcode_log_yyyymm` (
  `Fpkid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水编号（自增主键）',
  `Fmobile_no` varchar(32) NOT NULL COMMENT '手机号码',
  `Ftmpl_id` bigint(20) NOT NULL COMMENT '短信模板ID',
  `Fverify_code` varchar(16) NOT NULL COMMENT '验证码',
  `Frela_key` varchar(128) DEFAULT NULL COMMENT '关联key',
  `Frela_info` varchar(1024) DEFAULT NULL COMMENT '关联存储的信息',
  `Fchk_suc_times` int(11) DEFAULT '0' COMMENT '校验成功的次数',
  `Fchk_err_times` int(11) DEFAULT '0' COMMENT '校验失败的次数',
  `Fclient_ip` varchar(32) DEFAULT NULL COMMENT '用户IP',
  `Fsend_time` datetime DEFAULT NULL COMMENT '下发时间',
  `Fcheck_time` datetime DEFAULT NULL COMMENT '最后一次验证时间',
  `Fexpired_time` datetime DEFAULT NULL COMMENT '验证码过期时间',
  `Fstandby1` bigint(20) DEFAULT NULL COMMENT '预留字段',
  `Fstandby2` varchar(255) DEFAULT NULL COMMENT '预留字段',
  PRIMARY KEY (`Fpkid`),
  KEY `IDX_N_MOBILE_NO` (`Fmobile_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
