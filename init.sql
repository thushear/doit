-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.20-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 doit 的数据库结构
CREATE DATABASE IF NOT EXISTS `doit` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `doit`;

-- 导出  表 doit.author 结构
CREATE TABLE IF NOT EXISTS `author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.blog 结构
CREATE TABLE IF NOT EXISTS `blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `jhi_desc` varchar(255) DEFAULT NULL,
  `public_date` date DEFAULT NULL,
  `content` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.databasechangelog 结构
CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.databasechangeloglock 结构
CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_authority 结构
CREATE TABLE IF NOT EXISTS `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_persistent_audit_event 结构
CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(50) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_persistent_audit_evt_data 结构
CREATE TABLE IF NOT EXISTS `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(150) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_persistent_token 结构
CREATE TABLE IF NOT EXISTS `jhi_persistent_token` (
  `series` varchar(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `token_value` varchar(20) NOT NULL,
  `token_date` date DEFAULT NULL,
  `ip_address` varchar(39) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`series`),
  KEY `fk_user_persistent_token` (`user_id`),
  CONSTRAINT `fk_user_persistent_token` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_user 结构
CREATE TABLE IF NOT EXISTS `jhi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(6) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `idx_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`),
  UNIQUE KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.jhi_user_authority 结构
CREATE TABLE IF NOT EXISTS `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `USERNAME` varchar(30) NOT NULL,
  `PASSWORD` varchar(64) NOT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `EXPIRED` int(11) DEFAULT NULL,
  `DISABLE` int(11) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.sys_user_encode 结构
CREATE TABLE IF NOT EXISTS `sys_user_encode` (
  `USERNAME` varchar(30) NOT NULL,
  `PASSWORD` varchar(64) NOT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `EXPIRED` int(11) DEFAULT NULL,
  `DISABLED` int(11) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 doit.sys_user_question 结构
CREATE TABLE IF NOT EXISTS `sys_user_question` (
  `USERNAME` varchar(30) NOT NULL,
  `QUESTION` varchar(200) NOT NULL,
  `ANSWER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出 zipkin 的数据库结构
CREATE DATABASE IF NOT EXISTS `zipkin` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `zipkin`;

-- 导出  表 zipkin.zipkin_annotations 结构
CREATE TABLE IF NOT EXISTS `zipkin_annotations` (
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` blob COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` bigint(20) DEFAULT NULL COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_port` smallint(6) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` varchar(255) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  UNIQUE KEY `trace_id` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_4` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_7` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_10` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_13` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_16` (`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  KEY `trace_id_2` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_3` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4` (`endpoint_ipv4`) COMMENT 'for getTraces ordering',
  KEY `trace_id_5` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_6` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_2` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_2` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_2` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4_2` (`endpoint_ipv4`) COMMENT 'for getTraces ordering',
  KEY `trace_id_8` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_9` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_3` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_3` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_3` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4_3` (`endpoint_ipv4`) COMMENT 'for getTraces ordering',
  KEY `trace_id_11` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_12` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_4` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_4` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_4` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4_4` (`endpoint_ipv4`) COMMENT 'for getTraces ordering',
  KEY `trace_id_14` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_15` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_5` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_5` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_5` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4_5` (`endpoint_ipv4`) COMMENT 'for getTraces ordering',
  KEY `trace_id_17` (`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_18` (`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_6` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_6` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_6` (`a_key`) COMMENT 'for getTraces',
  KEY `endpoint_ipv4_6` (`endpoint_ipv4`) COMMENT 'for getTraces ordering'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- 数据导出被取消选择。
-- 导出  表 zipkin.zipkin_spans 结构
CREATE TABLE IF NOT EXISTS `zipkin_spans` (
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `debug` bit(1) DEFAULT NULL,
  `start_ts` bigint(20) DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  UNIQUE KEY `trace_id` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_4` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_7` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_10` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_13` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_16` (`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  KEY `trace_id_2` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_3` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_5` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_6` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_2` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_2` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_8` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_9` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_3` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_3` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_11` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_12` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_4` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_4` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_14` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_15` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_5` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_5` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_17` (`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_18` (`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_6` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_6` (`start_ts`) COMMENT 'for getTraces ordering and range'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
