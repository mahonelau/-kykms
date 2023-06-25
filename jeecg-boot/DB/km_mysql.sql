/*
 Navicat Premium Data Transfer

 Source Server         : ky_tencent
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 119.29.145.199:6033
 Source Schema         : km

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 23/06/2023 14:16:12
*/
CREATE database if NOT EXISTS `km` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `km`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for km_doc
-- ----------------------------
DROP TABLE IF EXISTS `km_doc`;
CREATE TABLE `km_doc`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `preview_file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `original_preview_file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_size` int(0) NULL DEFAULT NULL,
  `name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `serial_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` smallint(0) NULL DEFAULT NULL,
  `category` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keywords` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fti_flag` smallint(0) NULL DEFAULT NULL,
  `convert_flag` smallint(0) NULL DEFAULT NULL,
  `release_flag` smallint(0) NULL DEFAULT NULL,
  `downloads` bigint(0) NULL DEFAULT NULL,
  `views` bigint(0) NULL DEFAULT NULL,
  `last_update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `last_update_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `process_msg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dep_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pub_time_txt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `public_flag` smallint(0) NULL DEFAULT NULL,
  `download_flag` smallint(0) NULL DEFAULT NULL,
  `effect_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pub_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `public_remark` smallint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for km_doc_business_type
-- ----------------------------
DROP TABLE IF EXISTS `km_doc_business_type`;
CREATE TABLE `km_doc_business_type`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doc_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for km_doc_favourite
-- ----------------------------
DROP TABLE IF EXISTS `km_doc_favourite`;
CREATE TABLE `km_doc_favourite`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doc_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `add_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for km_doc_topic_type
-- ----------------------------
DROP TABLE IF EXISTS `km_doc_topic_type`;
CREATE TABLE `km_doc_topic_type`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doc_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `topic_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for km_doc_version
-- ----------------------------
DROP TABLE IF EXISTS `km_doc_version`;
CREATE TABLE `km_doc_version`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doc_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for km_doc_visit_record
-- ----------------------------
DROP TABLE IF EXISTS `km_doc_visit_record`;
CREATE TABLE `km_doc_visit_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `doc_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `visit_type` decimal(6, 0) NULL DEFAULT NULL COMMENT '1：上传 2：预览 3：下载 4：删除',
  `keywords` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `keywords_max` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `source_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_doc_visit_record
-- ----------------------------
INSERT INTO `km_doc_visit_record` VALUES ('402881ec88cd51160188cd51176c0002', '402881ec88cd51160188cd5116db0001', 0, '', '', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 15:03:44');
INSERT INTO `km_doc_visit_record` VALUES ('402881ec88cd52500188cd5250ce0000', '402881ec88cd51160188cd5116db0001', 7, '', '', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 15:05:04');
INSERT INTO `km_doc_visit_record` VALUES ('402881ec88cd52500188cd5266bb0003', '402881ec88cd52500188cd5266680002', 0, '', '', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 15:05:10');
INSERT INTO `km_doc_visit_record` VALUES ('402881ec88cd52500188cd5285320005', '402881ec88cd52500188cd5266680002', 5, '', '', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 15:05:17');
INSERT INTO `km_doc_visit_record` VALUES ('402881e888e6e29f0188e6e29fe30000', '402881ec88cd52500188cd5266680002', 5, '', '', '127.0.0.1', 'admin', '2023-06-23 14:13:12');
INSERT INTO `km_doc_visit_record` VALUES ('402881e888e6e29f0188e6e2ec9d0001', '402881ec88cd52500188cd5266680002', 5, '', '', '0:0:0:0:0:0:0:1', 'admin', '2023-06-23 14:13:31');

-- ----------------------------
-- Table structure for km_file
-- ----------------------------
DROP TABLE IF EXISTS `km_file`;
CREATE TABLE `km_file`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sha256` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `physical_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `url` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `original_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_file
-- ----------------------------
INSERT INTO `km_file` VALUES ('402881ec88cd52500188cd5266240001', '16c255987128afcc25aa713695e5b964b38ff97b839d1aedf79a0a6fcf25697e', '\\2023-06-18\\402881ec88cd52500188cd5266240001.docx', NULL, '槽边往事-7.2三叔十年.docx');
INSERT INTO `km_file` VALUES ('402881ec88cd52500188cd5275a70004', NULL, '\\2023-06-18\\pdf\\402881ec88cd52500188cd5266240001.pdf', NULL, '402881ec88cd52500188cd5266240001.pdf');

-- ----------------------------
-- Table structure for km_search_record
-- ----------------------------
DROP TABLE IF EXISTS `km_search_record`;
CREATE TABLE `km_search_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'id',
  `keywords` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'keywords',
  `title` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `topic_codes` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `keywords_max` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `source_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sourceIp',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'createBy',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'createTime'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_search_record
-- ----------------------------
INSERT INTO `km_search_record` VALUES ('1670316191024734210', '知识', '知识', '知识', NULL, '知识,知识,知识', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 14:23:08');
INSERT INTO `km_search_record` VALUES ('1670326831994974209', '往事', '往事', '往事', NULL, '往事,往事,往事', '0:0:0:0:0:0:0:1', 'admin', '2023-06-18 15:05:25');
INSERT INTO `km_search_record` VALUES ('1672125611878723585', '往事', '往事', '往事', NULL, '往事,往事,往事', '0:0:0:0:0:0:0:1', 'admin', '2023-06-23 14:13:07');

-- ----------------------------
-- Table structure for km_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `km_sys_config`;
CREATE TABLE `km_sys_config`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_sys_config
-- ----------------------------
INSERT INTO `km_sys_config` VALUES ('12', 'MaxContentSearchLength', '102400', '全文检索最大参数最大长度（内容查重，如果时间过长或报错，请调小参数值）', NULL);
INSERT INTO `km_sys_config` VALUES ('10', 'DuplicateCheckHitRate', '50%', '文档标题与内容查重关键字匹配比例(高于此比例为疑似重复)', NULL);
INSERT INTO `km_sys_config` VALUES ('1', 'supportFileTypes', 'doc,xls,pdf,ppt,txt,xlsx,docx,pptx,zip,rar,jpg,jpeg,bmp,png,gif', '全部支持的文件格式后缀，如果不限制上传格式则设置为空', NULL);
INSERT INTO `km_sys_config` VALUES ('4', 'downloadLimitConfig', '102', '24小时下载次数限制', NULL);
INSERT INTO `km_sys_config` VALUES ('5', 'TitleSearchBoostConfig', '1', '检索权重:标题,以1位基数', NULL);
INSERT INTO `km_sys_config` VALUES ('6', 'KeywordSearchBoostConfig', '1', '检索权重:关键字,以1位基数', NULL);
INSERT INTO `km_sys_config` VALUES ('7', 'ContentSearchBoostConfig', '1', '检索权重:内容,以1位基数', NULL);
INSERT INTO `km_sys_config` VALUES ('8', 'searchLimitConfig', '10', '10秒搜索次数限制', NULL);
INSERT INTO `km_sys_config` VALUES ('9', 'viewLimitConfig', '10', '10秒预览次数限制', NULL);
INSERT INTO `km_sys_config` VALUES ('2', 'IndexFileTypes', 'doc,xls,pdf,ppt,txt,xlsx,docx,pptx,zip,rar', '解释并全文入库的文件格式', NULL);
INSERT INTO `km_sys_config` VALUES ('3', 'ConvertFileTypes', 'doc,xls,pdf,ppt,txt,xlsx,docx,pptx,jpg,jpeg,bmp,png,gif', '自动进行预览文件转换的文件格式', NULL);
INSERT INTO `km_sys_config` VALUES ('11', 'RecommendHotTopic', '0', '是否在首页推荐热门专题', NULL);

-- ----------------------------
-- Table structure for onl_cgreport_head
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_head`;
CREATE TABLE `onl_cgreport_head`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表名字',
  `cgr_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表SQL',
  `return_val_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回值字段',
  `return_txt_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回文本字段',
  `return_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回类型，单选或多选',
  `db_source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动态数据源',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_head
-- ----------------------------
INSERT INTO `onl_cgreport_head` VALUES ('1420565897964433409', 'doc_visit_record', '文档操作记录', 'SELECT kd.serial_number,kd.title,kdvr.visit_type,kdvr.create_by,kdvr.source_ip,kdvr.create_time FROM km_doc_visit_record kdvr\nJOIN km_doc kd ON kd.id=kdvr.doc_id \n ORDER BY create_time DESC\n', NULL, NULL, '1', NULL, NULL, '2021-12-17 14:52:04', 'ceshi', '2021-07-29 10:04:38', 'admin');

-- ----------------------------
-- Table structure for onl_cgreport_item
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_item`;
CREATE TABLE `onl_cgreport_item`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表ID',
  `field_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `field_txt` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字段文本',
  `field_width` decimal(11, 0) NULL DEFAULT NULL,
  `field_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `search_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `is_order` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否排序  0否,1是',
  `is_search` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否查询  0否,1是',
  `dict_code` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字典CODE',
  `field_href` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段跳转URL',
  `is_show` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否显示  0否,1显示',
  `order_num` decimal(11, 0) NULL DEFAULT NULL COMMENT '排序',
  `replace_val` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取值表达式',
  `is_total` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否合计 0否,1是（仅对数值有效）',
  `group_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组标题',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_item
-- ----------------------------
INSERT INTO `onl_cgreport_item` VALUES ('1420565899138838529', '1420565897964433409', 'title', '文档标题', NULL, 'String', NULL, 0, 1, NULL, NULL, 1, 1, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1420565899155615745', '1420565897964433409', 'serial_number', '文档序列号', NULL, 'String', 'single', 0, 1, NULL, NULL, 1, 2, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1420565899168198658', '1420565897964433409', 'visit_type', '操作类型', NULL, 'Integer', 'single', 0, 1, 'dict_doc_visit_type', NULL, 1, 3, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1420565899168198659', '1420565897964433409', 'create_by', '操作人', NULL, 'String', 'single', 0, 1, '', NULL, 1, 4, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1420565899172392962', '1420565897964433409', 'source_ip', 'IP地址', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 5, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1420565899189170177', '1420565897964433409', 'create_time', '操作时间', NULL, 'Date', 'group', 0, 1, NULL, NULL, 1, 6, NULL, NULL, NULL, 'admin', '2021-07-29 10:04:38', NULL, NULL);

-- ----------------------------
-- Table structure for onl_cgreport_param
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_param`;
CREATE TABLE `onl_cgreport_param`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '动态报表ID',
  `param_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数字段',
  `param_txt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数文本',
  `param_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数默认值',
  `order_num` decimal(11, 0) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oss_file
-- ----------------------------
DROP TABLE IF EXISTS `oss_file`;
CREATE TABLE `oss_file`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Oss File' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `blob_data` longblob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_trig_to_trig_fk` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calendar` longblob NOT NULL,
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cron_expression` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time_zone_id` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_trig_to_trig_fk` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `entry_id` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fired_time` decimal(13, 0) NOT NULL,
  `sched_time` decimal(13, 0) NOT NULL,
  `priority` decimal(13, 0) NOT NULL,
  `state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE,
  INDEX `idx_qrtz_ft_inst_job_req_rcvry`(`instance_name`, `sched_name`, `requests_recovery`) USING BTREE,
  INDEX `idx_qrtz_ft_jg`(`job_group`, `sched_name`) USING BTREE,
  INDEX `idx_qrtz_ft_j_g`(`job_name`, `sched_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_ft_tg`(`sched_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_ft_trig_inst_name`(`sched_name`, `instance_name`) USING BTREE,
  INDEX `idx_qrtz_ft_t_g`(`sched_name`, `trigger_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job_class_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_durable` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_update_data` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_data` longblob NULL,
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_j_grp`(`sched_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_j_req_recovery`(`sched_name`, `requests_recovery`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lock_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('MyScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('MyScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_checkin_time` decimal(13, 0) NOT NULL,
  `checkin_interval` decimal(13, 0) NOT NULL,
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('MyScheduler', '172_16_0_21646914039944', 1647951194622, 10000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `repeat_count` decimal(7, 0) NOT NULL,
  `repeat_interval` decimal(12, 0) NOT NULL,
  `times_triggered` decimal(10, 0) NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_trig_to_trig_fk` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `str_prop_1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `str_prop_2` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `str_prop_3` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `int_prop_1` decimal(10, 0) NULL DEFAULT NULL,
  `int_prop_2` decimal(10, 0) NULL DEFAULT NULL,
  `long_prop_1` decimal(13, 0) NULL DEFAULT NULL,
  `long_prop_2` decimal(13, 0) NULL DEFAULT NULL,
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL,
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL,
  `bool_prop_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bool_prop_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_trig_to_trig_fk` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `next_fire_time` decimal(13, 0) NULL DEFAULT NULL,
  `prev_fire_time` decimal(13, 0) NULL DEFAULT NULL,
  `priority` decimal(13, 0) NULL DEFAULT NULL,
  `trigger_state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_time` decimal(13, 0) NOT NULL,
  `end_time` decimal(13, 0) NULL DEFAULT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `misfire_instr` decimal(2, 0) NULL DEFAULT NULL,
  `job_data` longblob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_t_c`(`sched_name`, `calendar_name`) USING BTREE,
  INDEX `idx_qrtz_t_g`(`sched_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_t_j`(`job_group`, `job_name`, `sched_name`) USING BTREE,
  INDEX `idx_qrtz_t_jg`(`sched_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_t_next_fire_time`(`sched_name`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_nft_misfire`(`sched_name`, `misfire_instr`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st`(`next_fire_time`, `sched_name`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire`(`sched_name`, `misfire_instr`, `next_fire_time`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire_grp`(`sched_name`, `misfire_instr`, `trigger_state`, `trigger_group`, `next_fire_time`) USING BTREE,
  INDEX `idx_qrtz_t_n_g_state`(`trigger_group`, `sched_name`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_n_state`(`sched_name`, `trigger_group`, `trigger_name`, `trigger_state`) USING BTREE,
  INDEX `idx_qrtz_t_state`(`sched_name`, `trigger_state`) USING BTREE,
  INDEX `qrtz_trigger_to_jobs_fk`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_trigger_to_jobs_fk` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `titile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `msg_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `sender` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `priority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级（L低，M中，H高）',
  `msg_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型1:通知公告2:系统消息',
  `msg_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
  `send_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布状态（0未发布，1已发布，2已撤销）',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '撤销时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
  `bus_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型(email:邮件 bpm:流程)',
  `bus_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `open_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打开方式(组件：component 路由：url)',
  `open_page` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件/路由 地址',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '指定用户',
  `msg_abstract` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '摘要',
  `dt_task_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '钉钉task_id，用于撤回消息'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统通告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_send`;
CREATE TABLE `sys_announcement_send`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `annt_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通告ID',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `read_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阅读状态（0未读，1已读）',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通告阅读标记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pid` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级节点',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `has_child` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否有子节点',
  `sort_order` int(0) NULL DEFAULT NULL,
  `recommend` tinyint(1) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_category
-- ----------------------------
INSERT INTO `sys_category` VALUES ('1450287793728155650', '1415239215563579394', '信息化项目', 'B04A02', 'ceshi', '2021-10-19 10:28:49', 'ceshi', '2021-10-19 10:28:58', 'A01', '1', 1, NULL);
INSERT INTO `sys_category` VALUES ('1450287947566837762', '1450287793728155650', '前置性审核', 'B04A02A05', 'ceshi', '2021-10-19 10:29:26', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450287992101957633', '1450287793728155650', '验收备案', 'B04A02A06', 'ceshi', '2021-10-19 10:29:37', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288184205275137', '1450288104739991553', '数据', 'B04A03A02', 'ceshi', '2021-10-19 10:30:22', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288234289459202', '1450288104739991553', '服务', 'B04A03A04', 'ceshi', '2021-10-19 10:30:34', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288263007858689', '1450288104739991553', '系统', 'B04A03A05', 'ceshi', '2021-10-19 10:30:41', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450287865912127490', '1450287793728155650', '立项审批', 'B04A02A02', 'ceshi', '2021-10-19 10:29:06', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1415239215563579394', '0', '教育信息化', 'B04', 'admin', '2021-07-14 04:18:18', 'ceshi', '2021-11-24 18:24:43', 'A01', '1', 1, 1);
INSERT INTO `sys_category` VALUES ('1450287893921689602', '1450287793728155650', '项目采购', 'B04A02A03', 'ceshi', '2021-10-19 10:29:13', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1448923409156202498', '1448923264612098049', '年度计划', 'B06A04', 'ceshi', '2021-10-15 16:07:15', 'ceshi', '2021-11-15 11:32:50', 'A01', NULL, 1, 0);
INSERT INTO `sys_category` VALUES ('1448923583488253953', '0', '教育管理', 'B07', 'ceshi', '2021-10-15 16:07:56', 'ceshi', '2021-11-23 14:00:03', 'A01', '0', 4, 0);
INSERT INTO `sys_category` VALUES ('1448923935583297538', '0', '教育理论', 'B10', 'ceshi', '2021-10-15 16:09:20', 'ceshi', '2021-11-23 14:00:10', 'A01', '0', 5, 1);
INSERT INTO `sys_category` VALUES ('1448923843216334849', '0', '素质教育', 'B08', 'ceshi', '2021-10-15 16:08:58', 'ceshi', '2021-11-23 14:00:36', 'A01', NULL, 7, 0);
INSERT INTO `sys_category` VALUES ('1448923896492384258', '0', '家校互动', 'B09', 'ceshi', '2021-10-15 16:09:11', 'ceshi', '2021-11-23 14:00:46', 'A01', NULL, 8, 0);
INSERT INTO `sys_category` VALUES ('1448924019523903490', '0', '教育实践', 'B11', 'ceshi', '2021-10-15 16:09:40', 'ceshi', '2021-11-23 14:00:20', 'A01', NULL, 6, NULL);
INSERT INTO `sys_category` VALUES ('1448924056215674881', '0', '学术研讨', 'B12', 'ceshi', '2021-10-15 16:09:49', 'ceshi', '2021-11-23 14:00:56', 'A01', NULL, 9, 0);
INSERT INTO `sys_category` VALUES ('1448924103653253122', '0', '样板课程', 'B13', 'ceshi', '2021-10-15 16:10:00', 'ceshi', '2021-11-23 14:02:40', 'A01', NULL, 15, NULL);
INSERT INTO `sys_category` VALUES ('1448924139699101697', '0', '体育教育', 'B14', 'ceshi', '2021-10-15 16:10:09', 'ceshi', '2021-11-23 14:02:01', 'A01', NULL, 11, 0);
INSERT INTO `sys_category` VALUES ('1448924177934376962', '0', '工作总结', 'B15', 'ceshi', '2021-10-15 16:10:18', 'ceshi', '2021-11-23 14:02:09', 'A01', NULL, 12, NULL);
INSERT INTO `sys_category` VALUES ('1448924211119710210', '0', '备课材料', 'B16', 'ceshi', '2021-10-15 16:10:26', 'ceshi', '2021-11-23 14:02:15', 'A01', NULL, 13, NULL);
INSERT INTO `sys_category` VALUES ('1448924255076016129', '0', '工作汇报', 'B17', 'ceshi', '2021-10-15 16:10:36', 'ceshi', '2021-11-23 14:02:24', 'A01', NULL, 14, NULL);
INSERT INTO `sys_category` VALUES ('1448924309627133953', '0', '社会反馈', 'B18', 'ceshi', '2021-10-15 16:10:49', 'ceshi', '2021-12-28 11:09:43', 'A01', '0', 10, 0);
INSERT INTO `sys_category` VALUES ('1448924350068613122', '0', '德智体发展', 'B19', 'ceshi', '2021-10-15 16:10:59', 'ceshi', '2021-11-23 14:03:04', 'A01', NULL, 16, NULL);
INSERT INTO `sys_category` VALUES ('1448924381949517826', '0', '国防教育', 'B20', 'ceshi', '2021-10-15 16:11:07', 'ceshi', '2021-11-23 14:03:17', 'A01', NULL, 17, NULL);
INSERT INTO `sys_category` VALUES ('1448924413280968705', '0', '人事政策', 'B21', 'ceshi', '2021-10-15 16:11:14', 'ceshi', '2021-11-23 14:03:32', 'A01', NULL, 18, NULL);
INSERT INTO `sys_category` VALUES ('1448924439432454145', '0', '其他', 'B22', 'ceshi', '2021-10-15 16:11:20', 'ceshi', '2021-10-21 16:46:11', 'A01', NULL, 19, NULL);
INSERT INTO `sys_category` VALUES ('1450287922069663745', '1450287793728155650', '云资源管理', 'B04A02A04', 'ceshi', '2021-10-19 10:29:20', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450287830122131457', '1450287793728155650', '预算编制', 'B04A02A01', 'ceshi', '2021-10-19 10:28:58', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288104739991553', '1415239215563579394', '信息化框架', 'B04A03', 'ceshi', '2021-10-19 10:30:03', 'ceshi', '2021-10-19 10:30:10', 'A01', '1', 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288134951563265', '1450288104739991553', '软硬件环境', 'B04A03A01', 'ceshi', '2021-10-19 10:30:11', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1450288212390998018', '1450288104739991553', '平台', 'B04A03A03', 'ceshi', '2021-10-19 10:30:29', NULL, NULL, 'A01', NULL, 1, NULL);
INSERT INTO `sys_category` VALUES ('1448919819863777282', '0', '教育研究', 'B05', 'ceshi', '2021-10-15 15:52:59', 'ceshi', '2021-11-24 18:24:39', 'A01', '0', 2, 1);
INSERT INTO `sys_category` VALUES ('1448923264612098049', '0', '经验交流', 'B06', 'ceshi', '2021-10-15 16:06:40', 'ceshi', '2021-12-28 11:17:28', 'A01', '1', 3, NULL);

-- ----------------------------
-- Table structure for sys_check_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_check_rule`;
CREATE TABLE `sys_check_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_json` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '规则JSON',
  `rule_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  UNIQUE INDEX `uk_scr_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_check_rule
-- ----------------------------
INSERT INTO `sys_check_rule` VALUES ('1224980593992388610', '通用编码规则', 'common', '[{\"digits\":\"1\",\"pattern\":\"^[a-z|A-Z]$\",\"message\":\"第一位只能是字母\"},{\"digits\":\"*\",\"pattern\":\"^[0-9|a-z|A-Z|_]{0,}$\",\"message\":\"只能填写数字、大小写字母、下划线\"},{\"digits\":\"*\",\"pattern\":\"^.{3,}$\",\"message\":\"最少输入3位数\"},{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"最多输入12位数\"}]', '规则：1、首位只能是字母；2、只能填写数字、大小写字母、下划线；3、最少3位数，最多12位数。', 'admin', '2020-02-07 11:25:48', 'admin', '2020-02-05 16:58:27');
INSERT INTO `sys_check_rule` VALUES ('1225001845524004866', '负责的功能测试', 'test', '[{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"只能输入3-12位字符\"},{\"digits\":\"3\",\"pattern\":\"^\\\\d{3}$\",\"message\":\"前3位必须是数字\"},{\"digits\":\"*\",\"pattern\":\"^[^pP]*$\",\"message\":\"不能输入P\"},{\"digits\":\"4\",\"pattern\":\"^@{4}$\",\"message\":\"第4-7位必须都为 @\"},{\"digits\":\"2\",\"pattern\":\"^#=$\",\"message\":\"第8-9位必须是 #=\"},{\"digits\":\"1\",\"pattern\":\"^O$\",\"message\":\"第10位必须为大写的O\"},{\"digits\":\"*\",\"pattern\":\"^.*。$\",\"message\":\"必须以。结尾\"}]', '包含长度校验、特殊字符校验等', 'admin', '2020-02-07 11:57:31', 'admin', '2020-02-05 18:22:54');

-- ----------------------------
-- Table structure for sys_data_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_log`;
CREATE TABLE `sys_data_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `data_table` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `data_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据ID',
  `data_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据内容',
  `data_version` decimal(11, 0) NULL DEFAULT NULL COMMENT '版本号',
  INDEX `idx_sdl_data_table_id`(`data_table`, `data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_data_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `db_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `db_driver` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驱动类',
  `db_url` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据源地址',
  `db_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `db_username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  UNIQUE INDEX `uk_sdc_rule_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_data_source
-- ----------------------------
INSERT INTO `sys_data_source` VALUES ('1209779538310004737', 'local_mysql', 'MySQL5.7', '本地数据库MySQL5.7', '1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false', 'jeecg-boot', 'root', 'f5b6775e8d1749483f2320627de0e706', 'admin', '2019-12-25 18:14:53', 'admin', '2020-07-10 16:54:42', 'A01');

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父机构ID',
  `depart_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构/部门名称',
  `depart_name_en` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '英文名',
  `depart_name_abbr` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '缩写',
  `depart_order` decimal(11, 0) NULL DEFAULT NULL COMMENT '排序',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `org_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构类别 1公司，2组织机构，2岗位',
  `org_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构类型 1一级部门 2子部门',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `memo` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（1启用，0不启用）',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
  `qywx_identifier` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对接企业微信的ID',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  UNIQUE INDEX `uniq_depart_org_code`(`org_code`) USING BTREE,
  INDEX `idx_sd_depart_order`(`depart_order`) USING BTREE,
  INDEX `idx_sd_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
INSERT INTO `sys_depart` VALUES ('f79b7d5a99b1442c876858a6961cb1fb', NULL, '默认部门', NULL, NULL, 0, NULL, '1', '1', 'A01', NULL, NULL, NULL, 'root', NULL, '0', NULL, 'ceshi', '2021-10-14 16:41:10', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('10c6c58575c24db297d838ec5c5f813d', NULL, '院长室', NULL, NULL, 0, NULL, '1', '1', 'A04', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-21 16:29:03', 'ceshi', '2021-10-21 16:29:15');
INSERT INTO `sys_depart` VALUES ('a06041ec62674810983326ae552ced92', NULL, '副院长室', NULL, NULL, 0, NULL, '1', '1', 'A05', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-21 16:30:10', 'ceshi', '2021-10-28 10:55:17');
INSERT INTO `sys_depart` VALUES ('e615307e7b684f15885ef6da29f6771d', 'a06041ec62674810983326ae552ced92', '一副院长', NULL, NULL, 1, NULL, '2', '2', 'A05A01', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:55:59', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('e0c42ad532b24b5fb76e44e9ecc03389', 'e615307e7b684f15885ef6da29f6771d', '人事财务室', NULL, NULL, 1, NULL, '2', '3', 'A05A01A01', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:58:15', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('2d9f7327dc2e4cf2b62d058a2c422534', 'e615307e7b684f15885ef6da29f6771d', '规划室', NULL, NULL, 2, NULL, '2', '3', 'A05A01A02', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:58:28', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('7aeb44fb58da45b484ab63b338acf738', 'e615307e7b684f15885ef6da29f6771d', '综合室', NULL, NULL, 3, NULL, '2', '3', 'A05A01A03', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:58:42', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('5eebbb70b6e24289933613b83c19c92b', 'a06041ec62674810983326ae552ced92', '二副院长', NULL, NULL, 2, NULL, '2', '2', 'A05A02', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:56:20', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('2b098c54e19943fb8246e1c1162a63a2', '5eebbb70b6e24289933613b83c19c92b', '园区管理办公室', NULL, NULL, 1, NULL, '2', '3', 'A05A02A01', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:59:21', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('8ef9c3c7e11e4fe0b2d457803b85cd24', 'a06041ec62674810983326ae552ced92', '三副院长', NULL, NULL, 3, NULL, '2', '2', 'A05A03', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:56:29', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('d28c13eaaa1d4655ac4a1cd248d10158', '8ef9c3c7e11e4fe0b2d457803b85cd24', '调查室', NULL, NULL, 1, NULL, '2', '3', 'A05A03A01', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:00:47', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('02e9449426374c099d59d31ba9142520', '8ef9c3c7e11e4fe0b2d457803b85cd24', '监测室', NULL, NULL, 2, NULL, '2', '3', 'A05A03A02', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:01:11', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('3995768f0b814cb6ba09a12f833c70f5', '8ef9c3c7e11e4fe0b2d457803b85cd24', '预报运维室', NULL, NULL, 3, NULL, '2', '3', 'A05A03A03', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:01:29', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('93732fbb0ff14965a337fc27972c6f4d', 'a06041ec62674810983326ae552ced92', '四副院长', NULL, NULL, 4, NULL, '2', '2', 'A05A04', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 10:56:40', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('35ccd833985b475a975e4b46eb4cf127', '93732fbb0ff14965a337fc27972c6f4d', '办公室', NULL, NULL, 1, NULL, '2', '3', 'A05A04A01', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:01:46', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('fb8b5f2a6d5d40508b9523434e44d536', '93732fbb0ff14965a337fc27972c6f4d', '政策研究室', NULL, NULL, 2, NULL, '2', '3', 'A05A04A02', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:01:57', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('c66be80d393b45af85ad2f1b307785e9', '93732fbb0ff14965a337fc27972c6f4d', '评价室', NULL, NULL, 3, NULL, '2', '3', 'A05A04A03', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'ceshi', '2021-10-28 11:02:12', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('1fbd9362d9cd4f3b9781794ca1f509b8', '', 'test', NULL, NULL, 0, NULL, '1', '1', 'A06', NULL, NULL, NULL, NULL, NULL, '0', NULL, 'admin', '2022-01-06 15:00:32', NULL, NULL);

-- ----------------------------
-- Table structure for sys_depart_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_permission`;
CREATE TABLE `sys_depart_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据规则id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_depart_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role`;
CREATE TABLE `sys_depart_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_depart_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_permission`;
CREATE TABLE `sys_depart_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据权限ids',
  `operate_date` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operate_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作ip',
  INDEX `idx_sdrp_per_id`(`permission_id`) USING BTREE,
  INDEX `idx_sdrp_role_id`(`role_id`) USING BTREE,
  INDEX `idx_sdrp_role_per_id`(`permission_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_depart_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_user`;
CREATE TABLE `sys_depart_role_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `drole_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` decimal(11, 0) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` decimal(11, 0) NULL DEFAULT NULL COMMENT '字典类型0为string,1为number',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('0b5d19e1fce4b2e6647e6b4a17760c14', '通告类型', 'msg_category', '消息类型1:通知公告2:系统消息', 0, 'admin', '2019-04-22 18:01:35', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174509082208395266', '职务职级', 'position_rank', '职务表职级字典', 0, 'admin', '2019-09-19 10:22:41', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174511106530525185', '机构类型', 'org_category', '机构类型 1公司，2部门 3岗位', 0, 'admin', '2019-09-19 10:30:43', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1178295274528845826', '表单权限策略', 'form_perms_type', NULL, 0, 'admin', '2019-09-29 21:07:39', 'admin', '2019-09-29 21:08:26', NULL);
INSERT INTO `sys_dict` VALUES ('1199517671259906049', '紧急程度', 'urgent_level', '日程计划紧急程度', 0, 'admin', '2019-11-27 10:37:53', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199518099888414722', '日程计划类型', 'eoa_plan_type', NULL, 0, 'admin', '2019-11-27 10:39:36', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199520177767587841', '分类栏目类型', 'eoa_cms_menu_type', NULL, 0, 'admin', '2019-11-27 10:47:51', 'admin', '2019-11-27 10:49:35', 0);
INSERT INTO `sys_dict` VALUES ('1199525215290306561', '日程计划状态', 'eoa_plan_status', NULL, 0, 'admin', '2019-11-27 11:07:52', 'admin', '2019-11-27 11:10:11', 0);
INSERT INTO `sys_dict` VALUES ('1209733563293962241', '数据库类型', 'database_type', NULL, 0, 'admin', '2019-12-25 15:12:12', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1232913193820581889', 'Online表单业务分类', 'ol_form_biz_type', NULL, 0, 'admin', '2020-02-27 14:19:46', 'admin', '2020-02-27 14:20:23', 0);
INSERT INTO `sys_dict` VALUES ('1250687930947620866', '定时任务状态', 'quartz_status', NULL, 0, 'admin', '2020-04-16 15:30:14', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES ('1280401766745718786', '租户状态', 'tenant_status', '租户状态', 0, 'admin', '2020-07-07 15:22:25', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1356445645198135298', '开关', 'is_open', NULL, 0, 'admin', '2021-02-02 11:33:38', 'admin', '2021-02-02 15:28:12', 0);
INSERT INTO `sys_dict` VALUES ('1415509679443714049', '涉及业务', 'km_dict_business', '涉及业务', 0, 'admin', '2021-07-14 22:13:01', 'admin', '2021-07-29 17:16:56', 0);
INSERT INTO `sys_dict` VALUES ('1415572544998932482', '文档来源', 'km_dict_source', '文档来源', 0, 'admin', '2021-07-15 02:22:49', 'admin', '2021-07-29 17:16:51', 0);
INSERT INTO `sys_dict` VALUES ('1415574243599777794', '分类目录', 'km_dict_category', '分类目录', 0, 'admin', '2021-07-15 02:29:34', 'admin', '2021-07-29 17:16:46', 0);
INSERT INTO `sys_dict` VALUES ('1415575033756319746', '文档版本状态', 'km_dict_versions', '文档版本状态', 0, 'admin', '2021-07-15 02:32:43', 'admin', '2021-07-29 17:16:40', 0);
INSERT INTO `sys_dict` VALUES ('1418495479619317761', '入库状态', 'dict_fti_flag', NULL, 0, 'admin', '2021-07-23 03:57:31', 'admin', '2021-07-23 04:04:33', 0);
INSERT INTO `sys_dict` VALUES ('1420563456950808577', '操作类型', 'dict_doc_visit_type', '操作日志-操作类型', 0, 'admin', '2021-07-29 09:54:56', 'admin', '2021-07-29 10:06:39', 0);
INSERT INTO `sys_dict` VALUES ('1435461054779015169', '文档状态', 'doc_dict_status', NULL, 0, 'admin', '2021-09-08 12:32:40', 'admin', '2021-09-08 12:46:04', 0);
INSERT INTO `sys_dict` VALUES ('1435763999777144834', '统计维度', 'dict_statisticsType', NULL, 0, 'admin', '2021-09-09 08:36:27', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1446749054125019137', '是否公开', 'dict_is_open', NULL, 1, 'admin', '2021-10-09 16:07:08', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1448189415518433282', '年份', 'km_dict_year', '发布年份', 0, 'admin', '2021-10-13 15:30:37', 'ceshi', '2021-10-14 23:12:52', 0);
INSERT INTO `sys_dict` VALUES ('1450412344901677057', '公开方式', 'km_dict_public_remark', '公开方式', 0, 'ceshi', '2021-10-19 18:43:45', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('236e8a4baff0db8c62c00dd95632834f', '同步工作流引擎', 'activiti_sync', '同步工作流引擎', 0, 'admin', '2019-05-15 15:27:33', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2e02df51611a4b9632828ab7e5338f00', '权限策略', 'perms_type', '权限策略', 0, 'admin', '2019-04-26 18:26:55', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2f0320997ade5dd147c90130f7218c3e', '推送类别', 'msg_type', NULL, 0, 'admin', '2019-03-17 21:21:32', 'admin', '2019-03-26 19:57:45', 0);
INSERT INTO `sys_dict` VALUES ('3486f32803bb953e7155dab3513dc68b', '删除状态', 'del_flag', NULL, 0, 'admin', '2019-01-18 21:46:26', 'admin', '2019-03-30 11:17:11', 0);
INSERT INTO `sys_dict` VALUES ('3d9a351be3436fbefb1307d4cfb49bf2', '性别', 'sex', NULL, 0, NULL, '2019-01-04 14:56:32', 'admin', '2019-03-30 11:28:27', 1);
INSERT INTO `sys_dict` VALUES ('4274efc2292239b6f000b153f50823ff', '全局权限策略', 'global_perms_type', '全局权限策略', 0, 'admin', '2019-05-10 17:54:05', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('4c03fca6bf1f0299c381213961566349', 'Online图表展示模板', 'online_graph_display_template', 'Online图表展示模板', 0, 'admin', '2019-04-12 17:28:50', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('4c753b5293304e7a445fd2741b46529d', '字典状态', 'dict_item_status', NULL, 0, 'admin', '2020-06-18 23:18:42', 'admin', '2019-03-30 19:33:52', 1);
INSERT INTO `sys_dict` VALUES ('4d7fec1a7799a436d26d02325eff295e', '优先级', 'priority', '优先级', 0, 'admin', '2019-03-16 17:03:34', 'admin', '2019-04-16 17:39:23', 0);
INSERT INTO `sys_dict` VALUES ('4e4602b3e3686f0911384e188dc7efb4', '条件规则', 'rule_conditions', NULL, 0, 'admin', '2019-04-01 10:15:03', 'admin', '2019-04-01 10:30:47', 0);
INSERT INTO `sys_dict` VALUES ('4f69be5f507accea8d5df5f11346181a', '发送消息类型', 'msgType', NULL, 0, 'admin', '2019-04-11 14:27:09', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('68168534ff5065a152bfab275c2136f8', '有效无效状态', 'valid_status', '有效无效状态', 0, 'admin', '2020-09-26 19:21:14', 'admin', '2019-04-26 19:21:23', 0);
INSERT INTO `sys_dict` VALUES ('6b78e3f59faec1a4750acff08030a79b', '用户类型', 'user_type', NULL, 0, NULL, '2019-01-04 14:59:01', 'admin', '2019-03-18 23:28:18', 0);
INSERT INTO `sys_dict` VALUES ('72cce0989df68887546746d8f09811aa', 'Online表单类型', 'cgform_table_type', NULL, 0, 'admin', '2019-01-27 10:13:02', 'admin', '2019-03-30 11:37:36', 0);
INSERT INTO `sys_dict` VALUES ('78bda155fe380b1b3f175f1e88c284c6', '流程状态', 'bpm_status', '流程状态', 0, 'admin', '2019-05-09 16:31:52', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('83bfb33147013cc81640d5fd9eda030c', '日志类型', 'log_type', NULL, 0, 'admin', '2019-03-18 23:22:19', NULL, NULL, 1);
INSERT INTO `sys_dict` VALUES ('845da5006c97754728bf48b6a10f79cc', '状态', 'status', NULL, 0, 'admin', '2019-03-18 21:45:25', 'admin', '2019-03-18 21:58:25', 0);
INSERT INTO `sys_dict` VALUES ('880a895c98afeca9d9ac39f29e67c13e', '操作类型', 'operate_type', '操作类型', 0, 'admin', '2019-07-22 10:54:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('8dfe32e2d29ea9430a988b3b558bf233', '发布状态', 'send_status', '发布状态', 0, 'admin', '2019-04-16 17:40:42', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a7adbcd86c37f7dbc9b66945c82ef9e6', '1是0否', 'yn', NULL, 0, 'admin', '2019-05-22 19:29:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a9d9942bd0eccb6e89de92d130ec4c4a', '消息发送状态', 'msgSendStatus', NULL, 0, 'admin', '2019-04-12 18:18:17', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('ac2f7c0c5c5775fcea7e2387bcb22f01', '菜单类型', 'menu_type', NULL, 0, 'admin', '2020-12-18 23:24:32', 'admin', '2019-04-01 15:27:06', 1);
INSERT INTO `sys_dict` VALUES ('ad7c65ba97c20a6805d5dcdf13cdaf36', 'onlineT类型', 'ceshi_online', NULL, 0, 'admin', '2019-03-22 16:31:49', 'admin', '2019-03-22 16:34:16', 0);
INSERT INTO `sys_dict` VALUES ('bd1b8bc28e65d6feefefb6f3c79f42fd', 'Online图表数据类型', 'online_graph_data_type', 'Online图表数据类型', 0, 'admin', '2019-04-12 17:24:24', 'admin', '2019-04-12 17:24:57', 0);
INSERT INTO `sys_dict` VALUES ('c36169beb12de8a71c8683ee7c28a503', '部门状态', 'depart_status', NULL, 0, 'admin', '2019-03-18 21:59:51', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('c5a14c75172783d72cbee6ee7f5df5d1', 'Online图表类型', 'online_graph_type', 'Online图表类型', 0, 'admin', '2019-04-12 17:04:06', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('d6e1152968b02d69ff358c75b48a6ee1', '流程类型', 'bpm_process_type', NULL, 0, 'admin', '2021-02-22 19:26:54', 'admin', '2019-03-30 18:14:44', 0);
INSERT INTO `sys_dict` VALUES ('fc6cd58fde2e8481db10d3a1e68ce70c', '用户状态', 'user_status', NULL, 0, 'admin', '2019-03-18 21:57:25', 'admin', '2019-03-18 23:11:58', 1);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dict_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项文本',
  `item_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort_order` decimal(11, 0) NULL DEFAULT NULL COMMENT '排序',
  `status` decimal(11, 0) NULL DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('f16c5706f3ae05c57a53850c64ce7c45', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送成功', '1', NULL, 2, 1, 'admin', '2019-04-12 18:19:43', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f2a7920421f3335afdf6ad2b342f6b5d', '845da5006c97754728bf48b6a10f79cc', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 21:46:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f37f90c496ec9841c4c326b065e00bb2', '83bfb33147013cc81640d5fd9eda030c', '登录日志', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:22:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f753aff60ff3931c0ecb4812d8b5e643', '4c03fca6bf1f0299c381213961566349', '双排布局', 'double', NULL, 3, 1, 'admin', '2019-04-12 17:43:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f80a8f6838215753b05e1a5ba3346d22', '880a895c98afeca9d9ac39f29e67c13e', '删除', '4', NULL, 4, 1, 'admin', '2019-07-22 10:55:14', 'admin', '2019-07-22 10:55:30');
INSERT INTO `sys_dict_item` VALUES ('fcec03570f68a175e1964808dc3f1c91', '4c03fca6bf1f0299c381213961566349', 'Tab风格', 'tab', NULL, 1, 1, 'admin', '2019-04-12 17:43:31', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('fe50b23ae5e68434def76f67cef35d2d', '78bda155fe380b1b3f175f1e88c284c6', '已作废', '4', '已作废', 4, 1, 'admin', '2021-09-09 16:33:43', 'admin', '2019-05-09 16:34:40');
INSERT INTO `sys_dict_item` VALUES ('1420564005536411661', '1420563456950808577', '修改', '6', '修改', 6, 1, 'admin', '2021-07-29 09:57:06', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1420564005536411664', '1420563456950808577', '删除', '7', '删除', 7, 1, 'admin', '2021-07-29 09:57:06', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1450412465588580354', '1450412344901677057', '主动公开', '1', NULL, 1, 1, NULL, '2021-10-19 18:44:14', 'ceshi', '2021-11-12 01:33:39');
INSERT INTO `sys_dict_item` VALUES ('1450412578524409857', '1450412344901677057', '依申请公开', '2', NULL, 2, 1, NULL, '2021-10-19 18:44:41', 'ceshi', '2021-10-19 19:10:43');
INSERT INTO `sys_dict_item` VALUES ('1450412635868934145', '1450412344901677057', '不公开', '3', NULL, 3, 1, NULL, '2021-10-19 18:44:54', 'ceshi', '2021-10-19 19:10:53');
INSERT INTO `sys_dict_item` VALUES ('0072d115e07c875d76c9b022e2179128', '4d7fec1a7799a436d26d02325eff295e', '低', 'L', '低', 3, 1, 'admin', '2019-04-16 17:04:59', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('05a2e732ce7b00aa52141ecc3e330b4e', '3486f32803bb953e7155dab3513dc68b', '已删除', '1', NULL, NULL, 1, 'admin', '2025-10-18 21:46:56', 'admin', '2019-03-28 22:23:20');
INSERT INTO `sys_dict_item` VALUES ('096c2e758d823def3855f6376bc736fb', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'SQL', 'sql', NULL, 1, 1, 'admin', '2019-04-12 17:26:26', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('0c9532916f5cd722017b46bc4d953e41', '2f0320997ade5dd147c90130f7218c3e', '指定用户', 'USER', NULL, NULL, 1, 'admin', '2019-03-17 21:22:19', 'admin', '2019-03-17 21:22:28');
INSERT INTO `sys_dict_item` VALUES ('0ca4beba9efc4f9dd54af0911a946d5c', '72cce0989df68887546746d8f09811aa', '附表', '3', NULL, 3, 1, 'admin', '2019-03-27 10:13:43', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1030a2652608f5eac3b49d70458b8532', '2e02df51611a4b9632828ab7e5338f00', '禁用', '2', '禁用', 2, 1, 'admin', '2021-03-26 18:27:28', 'admin', '2019-04-26 18:39:11');
INSERT INTO `sys_dict_item` VALUES ('1174509082208395266', '1174511106530525185', '岗位', '3', '岗位', 1, 1, 'admin', '2019-09-19 10:31:16', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1174509601047994369', '1174509082208395266', '员级', '1', NULL, 1, 1, 'admin', '2019-09-19 10:24:45', 'admin', '2019-09-23 11:46:39');
INSERT INTO `sys_dict_item` VALUES ('1174509667297026049', '1174509082208395266', '助级', '2', NULL, 2, 1, 'admin', '2019-09-19 10:25:01', 'admin', '2019-09-23 11:46:47');
INSERT INTO `sys_dict_item` VALUES ('1174509713568587777', '1174509082208395266', '中级', '3', NULL, 3, 1, 'admin', '2019-09-19 10:25:12', 'admin', '2019-09-23 11:46:56');
INSERT INTO `sys_dict_item` VALUES ('1174509788361416705', '1174509082208395266', '副高级', '4', NULL, 4, 1, 'admin', '2019-09-19 10:25:30', 'admin', '2019-09-23 11:47:06');
INSERT INTO `sys_dict_item` VALUES ('1174509835803189250', '1174509082208395266', '正高级', '5', NULL, 5, 1, 'admin', '2019-09-19 10:25:41', 'admin', '2019-09-23 11:47:12');
INSERT INTO `sys_dict_item` VALUES ('1174511197735665665', '1174511106530525185', '公司', '1', '公司', 1, 1, 'admin', '2019-09-19 10:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1174511244036587521', '1174511106530525185', '部门', '2', '部门', 1, 1, 'admin', '2019-09-19 10:31:16', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1178295553450061826', '1178295274528845826', '可编辑(未授权禁用)', '2', NULL, 2, 1, 'admin', '2019-09-29 21:08:46', 'admin', '2019-09-29 21:09:18');
INSERT INTO `sys_dict_item` VALUES ('1178295639554928641', '1178295274528845826', '可见(未授权不可见)', '1', NULL, 1, 1, 'admin', '2019-09-29 21:09:06', 'admin', '2019-09-29 21:09:24');
INSERT INTO `sys_dict_item` VALUES ('1199517884758368257', '1199517671259906049', '一般', '1', NULL, 1, 1, 'admin', '2019-11-27 10:38:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517914017832962', '1199517671259906049', '重要', '2', NULL, 1, 1, 'admin', '2019-11-27 10:38:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517941339529217', '1199517671259906049', '紧急', '3', NULL, 1, 1, 'admin', '2019-11-27 10:38:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518186144276482', '1199518099888414722', '日常记录', '1', NULL, 1, 1, 'admin', '2019-11-27 10:39:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518214858481666', '1199518099888414722', '本周工作', '2', NULL, 1, 1, 'admin', '2019-11-27 10:40:03', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518235943247874', '1199518099888414722', '下周计划', '3', NULL, 1, 1, 'admin', '2019-11-27 10:40:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520817285701634', '1199520177767587841', '列表', '1', NULL, 1, 1, 'admin', '2019-11-27 10:50:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520835035996161', '1199520177767587841', '链接', '2', NULL, 1, 1, 'admin', '2019-11-27 10:50:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525468672405505', '1199525215290306561', '未开始', '0', NULL, 1, 1, 'admin', '2019-11-27 11:08:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525490575060993', '1199525215290306561', '进行中', '1', NULL, 1, 1, 'admin', '2019-11-27 11:08:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525506429530114', '1199525215290306561', '已完成', '2', NULL, 1, 1, 'admin', '2019-11-27 11:09:02', 'admin', '2019-11-27 11:10:02');
INSERT INTO `sys_dict_item` VALUES ('1199607547704647681', '4f69be5f507accea8d5df5f11346181a', '系统', '4', NULL, 1, 1, 'admin', '2019-11-27 16:35:02', 'admin', '2019-11-27 19:37:46');
INSERT INTO `sys_dict_item` VALUES ('1209733775114702850', '1209733563293962241', 'MySQL5.5', '1', NULL, 1, 1, 'admin', '2019-12-25 15:13:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733839933476865', '1209733563293962241', 'Oracle', '2', NULL, 3, 1, 'admin', '2019-12-25 15:13:18', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733903020003330', '1209733563293962241', 'SQLServer', '3', NULL, 4, 1, 'admin', '2019-12-25 15:13:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1232913424813486081', '1232913193820581889', '官方示例', 'demo', NULL, 1, 1, 'admin', '2020-02-27 14:20:42', 'admin', '2020-02-27 14:21:37');
INSERT INTO `sys_dict_item` VALUES ('1232913493717512194', '1232913193820581889', '流程表单', 'bpm', NULL, 2, 1, 'admin', '2020-02-27 14:20:58', 'admin', '2020-02-27 14:22:20');
INSERT INTO `sys_dict_item` VALUES ('1232913605382467585', '1232913193820581889', '测试表单', 'temp', NULL, 4, 1, 'admin', '2020-02-27 14:21:25', 'admin', '2020-02-27 14:22:16');
INSERT INTO `sys_dict_item` VALUES ('1232914232372195330', '1232913193820581889', '导入表单', 'bdfl_include', NULL, 5, 1, 'admin', '2020-02-27 14:23:54', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1234371726545010689', '4e4602b3e3686f0911384e188dc7efb4', '左模糊', 'LEFT_LIKE', '左模糊', 7, 1, 'admin', '2020-03-02 14:55:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1234371809495760898', '4e4602b3e3686f0911384e188dc7efb4', '右模糊', 'RIGHT_LIKE', '右模糊', 7, 1, 'admin', '2020-03-02 14:55:47', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1250688147579228161', '1250687930947620866', '正常', '0', NULL, 1, 1, 'admin', '2020-04-16 15:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1250688201064992770', '1250687930947620866', '停止', '-1', NULL, 1, 1, 'admin', '2020-04-16 15:31:18', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1280401815068295170', '1280401766745718786', '正常', '1', NULL, 1, 1, 'admin', '2020-07-07 15:22:36', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1280401847607705602', '1280401766745718786', '冻结', '0', NULL, 1, 1, 'admin', '2020-07-07 15:22:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1305827309355302914', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'API', 'api', NULL, 3, 1, 'admin', '2020-09-15 19:14:26', 'admin', '2020-09-15 19:14:41');
INSERT INTO `sys_dict_item` VALUES ('1334440962954936321', '1209733563293962241', 'MYSQL5.7', '4', NULL, 1, 1, 'admin', '2020-12-03 18:16:02', 'admin', '2020-12-03 18:16:02');
INSERT INTO `sys_dict_item` VALUES ('1356445705549975553', '1356445645198135298', '是', 'Y', NULL, 1, 1, 'admin', '2021-02-02 11:33:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1356445754212290561', '1356445645198135298', '否', 'N', NULL, 1, 1, 'admin', '2021-02-02 11:34:04', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415510117652013057', '1415509679443714049', '工作汇报', '1', '', 1, 1, 'admin', '2021-07-14 22:14:46', 'ceshi', '2021-11-23 11:50:48');
INSERT INTO `sys_dict_item` VALUES ('1415512755764371457', '1415509679443714049', '教学研究', '2', '', 1, 1, 'admin', '2021-07-14 22:25:15', 'ceshi', '2021-11-23 11:51:07');
INSERT INTO `sys_dict_item` VALUES ('1415512840657084418', '1415509679443714049', '教学材料', '3', '', 1, 1, 'admin', '2021-07-14 22:25:35', 'ceshi', '2021-11-23 11:51:44');
INSERT INTO `sys_dict_item` VALUES ('1415512908009218049', '1415509679443714049', '行业交流', '4', '', 1, 1, 'admin', '2021-07-14 22:25:51', 'ceshi', '2021-11-23 11:52:07');
INSERT INTO `sys_dict_item` VALUES ('1415512988313362433', '1415509679443714049', '政策研究', '5', NULL, 1, 1, 'admin', '2021-07-14 22:26:10', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415513052456853505', '1415509679443714049', '学生档案', '6', '', 1, 1, 'admin', '2021-07-14 22:26:25', 'ceshi', '2021-11-23 11:54:12');
INSERT INTO `sys_dict_item` VALUES ('1415513104826933249', '1415509679443714049', '备课材料', '7', '', 1, 1, 'admin', '2021-07-14 22:26:38', 'ceshi', '2021-11-23 11:54:29');
INSERT INTO `sys_dict_item` VALUES ('1415518346117292034', '1415509679443714049', '会议纪要', '8', '', 1, 1, 'admin', '2021-07-14 22:47:27', 'ceshi', '2021-11-23 11:54:44');
INSERT INTO `sys_dict_item` VALUES ('1415518423493812226', '1415509679443714049', '政府文件', '9', '', 1, 1, 'admin', '2021-07-14 22:47:46', 'ceshi', '2021-11-23 13:52:59');
INSERT INTO `sys_dict_item` VALUES ('1415518535649501185', '1415509679443714049', '考试题库', '10', '', 1, 1, 'admin', '2021-07-14 22:48:13', 'ceshi', '2021-11-23 13:53:32');
INSERT INTO `sys_dict_item` VALUES ('1415518651982716929', '1415509679443714049', '进修', '11', '', 1, 0, 'admin', '2021-07-14 22:48:40', 'ceshi', '2021-11-23 13:57:07');
INSERT INTO `sys_dict_item` VALUES ('1415518739014524929', '1415509679443714049', '继续教育', '12', '', 1, 0, 'admin', '2021-07-14 22:49:01', 'ceshi', '2021-11-23 13:57:17');
INSERT INTO `sys_dict_item` VALUES ('1415518841401679874', '1415509679443714049', '听课评课', '13', '', 1, 0, 'admin', '2021-07-14 22:49:26', 'ceshi', '2021-11-23 13:57:27');
INSERT INTO `sys_dict_item` VALUES ('1415518946661933057', '1415509679443714049', '教育实践', '14', '', 1, 0, 'admin', '2021-07-14 22:49:51', 'ceshi', '2021-11-23 13:58:04');
INSERT INTO `sys_dict_item` VALUES ('1415519089138245634', '1415509679443714049', '联合办学', '15', '', 1, 0, 'admin', '2021-07-14 22:50:25', 'ceshi', '2021-11-23 13:58:12');
INSERT INTO `sys_dict_item` VALUES ('1415519256226734082', '1415509679443714049', '学校管理', '16', '', 1, 0, 'admin', '2021-07-14 22:51:04', 'ceshi', '2021-11-23 13:58:29');
INSERT INTO `sys_dict_item` VALUES ('1415519311788679170', '1415509679443714049', '职称考评', '17', '', 1, 0, 'admin', '2021-07-14 22:51:18', 'ceshi', '2021-11-23 13:58:45');
INSERT INTO `sys_dict_item` VALUES ('1415519414536544257', '1415509679443714049', '人事政策', '18', '', 1, 0, 'admin', '2021-07-14 22:51:42', 'ceshi', '2021-11-23 13:58:57');
INSERT INTO `sys_dict_item` VALUES ('1415519478847807490', '1415509679443714049', '其他', '19', '', 11, 1, 'admin', '2021-07-14 22:51:57', 'ceshi', '2021-11-23 13:55:27');
INSERT INTO `sys_dict_item` VALUES ('1415572705485586434', '1415572544998932482', '中央国务院全国人大', '1', NULL, 1, 1, 'admin', '2021-07-15 02:23:28', 'ceshi', '2021-10-15 16:44:35');
INSERT INTO `sys_dict_item` VALUES ('1415572764965011458', '1415572544998932482', '自然资源部', '2', NULL, 1, 1, 'admin', '2021-07-15 02:23:42', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415572821936242690', '1415572544998932482', '其他部委', '3', NULL, 1, 1, 'admin', '2021-07-15 02:23:55', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415572891293253634', '1415572544998932482', '省委省府省人大', '4', NULL, 1, 1, 'admin', '2021-07-15 02:24:12', 'ceshi', '2021-10-15 16:44:49');
INSERT INTO `sys_dict_item` VALUES ('1415572979956645890', '1415572544998932482', '省自然资源厅', '5', NULL, 1, 1, 'admin', '2021-07-15 02:24:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573174215835649', '1415572544998932482', '其他省直单位', '6', NULL, 1, 1, 'admin', '2021-07-15 02:25:19', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573266092064770', '1415572544998932482', '省土地调查规划院', '7', NULL, 1, 1, 'admin', '2021-07-15 02:25:41', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573399961665538', '1415572544998932482', '省内其他单位', '8', NULL, 1, 1, 'admin', '2021-07-15 02:26:13', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573454684749826', '1415572544998932482', '外省单位', '9', NULL, 1, 1, 'admin', '2021-07-15 02:26:26', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573499555414018', '1415572544998932482', '境外', '10', NULL, 1, 1, 'admin', '2021-07-15 02:26:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573567788351489', '1415572544998932482', '媒体', '11', NULL, 1, 1, 'admin', '2021-07-15 02:26:53', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415573676152389634', '1415572544998932482', '个人', '12', NULL, 1, 1, 'admin', '2021-07-15 02:27:19', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415574342098812929', '1415574243599777794', '语文', '1', '', 1, 1, 'admin', '2021-07-15 02:29:58', 'ceshi', '2021-11-23 11:39:09');
INSERT INTO `sys_dict_item` VALUES ('1415574391214112769', '1415574243599777794', '数学', '2', '', 1, 1, 'admin', '2021-07-15 02:30:10', 'ceshi', '2021-11-23 11:39:18');
INSERT INTO `sys_dict_item` VALUES ('1415574432960020481', '1415574243599777794', '英语', '3', '', 1, 1, 'admin', '2021-07-15 02:30:20', 'ceshi', '2021-11-23 11:39:24');
INSERT INTO `sys_dict_item` VALUES ('1415574502241533954', '1415574243599777794', '品德', '4', '', 1, 1, 'admin', '2021-07-15 02:30:36', 'ceshi', '2021-11-23 11:39:43');
INSERT INTO `sys_dict_item` VALUES ('1415574564849909762', '1415574243599777794', '体育', '5', '', 1, 1, 'admin', '2021-07-15 02:30:51', 'ceshi', '2021-11-23 11:39:50');
INSERT INTO `sys_dict_item` VALUES ('1415574697549299713', '1415574243599777794', '美术', '7', '', 1, 1, 'admin', '2021-07-15 02:31:23', 'ceshi', '2021-11-23 11:40:23');
INSERT INTO `sys_dict_item` VALUES ('1415575114165321729', '1415575033756319746', '最终稿', '1', NULL, 1, 1, 'admin', '2021-07-15 02:33:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1415575176530427906', '1415575033756319746', '过程稿', '2', NULL, 1, 1, 'admin', '2021-07-15 02:33:17', 'ceshi', '2021-10-26 17:26:24');
INSERT INTO `sys_dict_item` VALUES ('1418496971277398018', '1418495479619317761', '入库失败', '-1', NULL, 1, 1, 'admin', '2021-07-23 04:03:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1418497014818467842', '1418495479619317761', '等待入库', '0', NULL, 1, 1, 'admin', '2021-07-23 04:03:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1418497058619584514', '1418495479619317761', '已入库', '1', NULL, 1, 1, 'admin', '2021-07-23 04:03:48', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1418497118333890561', '1418495479619317761', '不需要入库', '2', NULL, 1, 1, 'admin', '2021-07-23 04:04:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1420563600144347137', '1420563456950808577', '上传', '0', '上传', 0, 1, 'admin', '2021-07-29 09:55:30', 'admin', '2021-07-29 09:55:57');
INSERT INTO `sys_dict_item` VALUES ('1420563852746305538', '1420563456950808577', '修改预览文档', '3', '修改预览文档', 3, 1, 'admin', '2021-07-29 09:56:30', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1420563930554839041', '1420563456950808577', '下载', '4', '下载', 4, 1, 'admin', '2021-07-29 09:56:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1420564005536411650', '1420563456950808577', '预览', '5', '预览', 5, 1, 'admin', '2021-07-29 09:57:06', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435461449668542466', '1435461054779015169', '草稿', '0', NULL, 1, 1, 'admin', '2021-09-08 12:34:14', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435461470677811202', '1435461054779015169', '待审核', '1', NULL, 1, 1, 'admin', '2021-09-08 12:34:19', 'admin', '2021-09-08 12:35:03');
INSERT INTO `sys_dict_item` VALUES ('1435461526139092994', '1435461054779015169', '审核通过', '2', NULL, 1, 1, 'admin', '2021-09-08 12:34:32', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435461599442944002', '1435461054779015169', '审核驳回', '3', NULL, 1, 1, 'admin', '2021-09-08 12:34:50', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435764110502576129', '1435763999777144834', '分类', '1', NULL, 1, 1, 'admin', '2021-09-09 08:36:54', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435764144711319554', '1435763999777144834', '来源', '2', NULL, 1, 1, 'admin', '2021-09-09 08:37:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435764240131735554', '1435763999777144834', '涉及业务', '4', NULL, 1, 1, 'admin', '2021-09-09 08:37:25', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1435764293730746370', '1435763999777144834', '版本状态', '5', NULL, 1, 1, 'admin', '2021-09-09 08:37:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1446749165269880833', '1446749054125019137', '公开', '0', NULL, 1, 1, 'admin', '2021-10-09 16:07:35', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1446749218503987201', '1446749054125019137', '不公开', '1', NULL, 1, 1, 'admin', '2021-10-09 16:07:48', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448189485705916417', '1448189415518433282', '2021年', '2021', NULL, 2021, 1, NULL, '2021-10-13 15:30:54', NULL, '2021-10-13 15:31:47');
INSERT INTO `sys_dict_item` VALUES ('1448189539787272194', '1448189415518433282', '2022年', '2022', NULL, 2022, 1, NULL, '2021-10-13 15:31:07', NULL, '2021-10-13 15:31:59');
INSERT INTO `sys_dict_item` VALUES ('1448189588860628993', '1448189415518433282', '2020年', '2020', NULL, 2020, 1, NULL, '2021-10-13 15:31:19', NULL, '2021-10-13 15:31:54');
INSERT INTO `sys_dict_item` VALUES ('1448199169691152386', '1448189415518433282', '2019年', '2019', NULL, 2019, 1, NULL, '2021-10-13 16:09:23', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199236258951170', '1448189415518433282', '2018年', '2018', NULL, 2018, 1, NULL, '2021-10-13 16:09:39', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199306417074178', '1448189415518433282', '2023年', '2023', NULL, 2023, 1, NULL, '2021-10-13 16:09:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199379939028994', '1448189415518433282', '2024年', '2024', NULL, 2024, 1, NULL, '2021-10-13 16:10:13', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199445483417601', '1448189415518433282', '2025年', '2025', NULL, 2025, 1, NULL, '2021-10-13 16:10:29', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199700622929921', '1448189415518433282', '2017年', '2017', NULL, 2017, 1, NULL, '2021-10-13 16:11:30', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199760270127105', '1448189415518433282', '2016年', '2016', NULL, 2016, 1, NULL, '2021-10-13 16:11:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199814963851265', '1448189415518433282', '2015年', '2015', NULL, 2015, 1, NULL, '2021-10-13 16:11:57', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199899512631298', '1448189415518433282', '2026年', '2026', NULL, 2026, 1, NULL, '2021-10-13 16:12:17', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448199976788488193', '1448189415518433282', '2027年', '2027', NULL, 2027, 1, NULL, '2021-10-13 16:12:35', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448200037769474050', '1448189415518433282', '2028年', '2028', NULL, 2028, 1, NULL, '2021-10-13 16:12:50', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1448200113774456834', '1448189415518433282', '2029年', '2029', NULL, 2029, 1, NULL, '2021-10-13 16:13:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('147c48ff4b51545032a9119d13f3222a', 'd6e1152968b02d69ff358c75b48a6ee1', '测试流程', 'test', NULL, 1, 1, 'admin', '2019-03-22 19:27:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1543fe7e5e26fb97cdafe4981bedc0c8', '4c03fca6bf1f0299c381213961566349', '单排布局', 'single', NULL, 2, 1, 'admin', '2022-07-12 17:43:39', 'admin', '2019-04-12 17:43:57');
INSERT INTO `sys_dict_item` VALUES ('1ce390c52453891f93514c1bd2795d44', 'ad7c65ba97c20a6805d5dcdf13cdaf36', '000', '00', NULL, 1, 1, 'admin', '2019-03-22 16:34:34', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1db531bcff19649fa82a644c8a939dc4', '4c03fca6bf1f0299c381213961566349', '组合布局', 'combination', NULL, 4, 1, 'admin', '2019-05-11 16:07:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('222705e11ef0264d4214affff1fb4ff9', '4f69be5f507accea8d5df5f11346181a', '短信', '1', NULL, 1, 1, 'admin', '2023-02-28 10:50:36', 'admin', '2019-04-28 10:58:11');
INSERT INTO `sys_dict_item` VALUES ('23a5bb76004ed0e39414e928c4cde155', '4e4602b3e3686f0911384e188dc7efb4', '不等于', '!=', '不等于', 3, 1, 'admin', '2019-04-01 16:46:15', 'admin', '2019-04-01 17:48:40');
INSERT INTO `sys_dict_item` VALUES ('25847e9cb661a7c711f9998452dc09e6', '4e4602b3e3686f0911384e188dc7efb4', '小于等于', '<=', '小于等于', 6, 1, 'admin', '2019-04-01 16:44:34', 'admin', '2019-04-01 17:49:10');
INSERT INTO `sys_dict_item` VALUES ('2d51376643f220afdeb6d216a8ac2c01', '68168534ff5065a152bfab275c2136f8', '有效', '1', '有效', 2, 1, 'admin', '2019-04-26 19:22:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('308c8aadf0c37ecdde188b97ca9833f5', '8dfe32e2d29ea9430a988b3b558bf233', '已发布', '1', '已发布', 2, 1, 'admin', '2019-04-16 17:41:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('333e6b2196e01ef9a5f76d74e86a6e33', '8dfe32e2d29ea9430a988b3b558bf233', '未发布', '0', '未发布', 1, 1, 'admin', '2019-04-16 17:41:12', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('337ea1e401bda7233f6258c284ce4f50', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'JSON', 'json', NULL, 1, 1, 'admin', '2019-04-12 17:26:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('33bc9d9f753cf7dc40e70461e50fdc54', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送失败', '2', NULL, 3, 1, 'admin', '2019-04-12 18:20:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('3fbc03d6c994ae06d083751248037c0e', '78bda155fe380b1b3f175f1e88c284c6', '已完成', '3', '已完成', 3, 1, 'admin', '2019-05-09 16:33:25', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41d7aaa40c9b61756ffb1f28da5ead8e', '0b5d19e1fce4b2e6647e6b4a17760c14', '通知公告', '1', NULL, 1, 1, 'admin', '2019-04-22 18:01:57', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41fa1e9571505d643aea87aeb83d4d76', '4e4602b3e3686f0911384e188dc7efb4', '等于', '=', '等于', 4, 1, 'admin', '2019-04-01 16:45:24', 'admin', '2019-04-01 17:49:00');
INSERT INTO `sys_dict_item` VALUES ('43d2295b8610adce9510ff196a49c6e9', '845da5006c97754728bf48b6a10f79cc', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 21:45:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('4f05fb5376f4c61502c5105f52e4dd2b', '83bfb33147013cc81640d5fd9eda030c', '操作日志', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:22:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('50223341bfb5ba30bf6319789d8d17fe', 'd6e1152968b02d69ff358c75b48a6ee1', '业务办理', 'business', NULL, 3, 1, 'admin', '2023-04-22 19:28:05', 'admin', '2019-03-22 23:24:39');
INSERT INTO `sys_dict_item` VALUES ('51222413e5906cdaf160bb5c86fb827c', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '是', '1', NULL, 1, 1, 'admin', '2019-05-22 19:29:45', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('538fca35afe004972c5f3947c039e766', '2e02df51611a4b9632828ab7e5338f00', '显示', '1', '显示', 1, 1, 'admin', '2025-03-26 18:27:13', 'admin', '2019-04-26 18:39:07');
INSERT INTO `sys_dict_item` VALUES ('5584c21993bde231bbde2b966f2633ac', '4e4602b3e3686f0911384e188dc7efb4', '自定义SQL表达式', 'USE_SQL_RULES', '自定义SQL表达式', 9, 1, 'admin', '2019-04-01 10:45:24', 'admin', '2019-04-01 17:49:27');
INSERT INTO `sys_dict_item` VALUES ('58b73b344305c99b9d8db0fc056bbc0a', '72cce0989df68887546746d8f09811aa', '主表', '2', NULL, 2, 1, 'admin', '2019-03-27 10:13:36', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('5b65a88f076b32e8e69d19bbaadb52d5', '2f0320997ade5dd147c90130f7218c3e', '全体用户', 'ALL', NULL, NULL, 1, 'admin', '2020-10-17 21:22:43', 'admin', '2019-03-28 22:17:09');
INSERT INTO `sys_dict_item` VALUES ('5d833f69296f691843ccdd0c91212b6b', '880a895c98afeca9d9ac39f29e67c13e', '修改', '3', NULL, 3, 1, 'admin', '2019-07-22 10:55:07', 'admin', '2019-07-22 10:55:41');
INSERT INTO `sys_dict_item` VALUES ('5d84a8634c8fdfe96275385075b105c9', '3d9a351be3436fbefb1307d4cfb49bf2', '女', '2', NULL, 2, 1, NULL, '2019-01-04 14:56:56', NULL, '2019-01-04 17:38:12');
INSERT INTO `sys_dict_item` VALUES ('66c952ae2c3701a993e7db58f3baf55e', '4e4602b3e3686f0911384e188dc7efb4', '大于', '>', '大于', 1, 1, 'admin', '2019-04-01 10:45:46', 'admin', '2019-04-01 17:48:29');
INSERT INTO `sys_dict_item` VALUES ('6937c5dde8f92e9a00d4e2ded9198694', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'easyui', '3', NULL, 1, 1, 'admin', '2019-03-22 16:32:15', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('69cacf64e244100289ddd4aa9fa3b915', 'a9d9942bd0eccb6e89de92d130ec4c4a', '未发送', '0', NULL, 1, 1, 'admin', '2019-04-12 18:19:23', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('6a7a9e1403a7943aba69e54ebeff9762', '4f69be5f507accea8d5df5f11346181a', '邮件', '2', NULL, 2, 1, 'admin', '2031-02-28 10:50:44', 'admin', '2019-04-28 10:59:03');
INSERT INTO `sys_dict_item` VALUES ('6c682d78ddf1715baf79a1d52d2aa8c2', '72cce0989df68887546746d8f09811aa', '单表', '1', NULL, 1, 1, 'admin', '2019-03-27 10:13:29', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('6d404fd2d82311fbc87722cd302a28bc', '4e4602b3e3686f0911384e188dc7efb4', '模糊', 'LIKE', '模糊', 7, 1, 'admin', '2019-04-01 16:46:02', 'admin', '2019-04-01 17:49:20');
INSERT INTO `sys_dict_item` VALUES ('6d4e26e78e1a09699182e08516c49fc4', '4d7fec1a7799a436d26d02325eff295e', '高', 'H', '高', 1, 1, 'admin', '2019-04-16 17:04:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('700e9f030654f3f90e9ba76ab0713551', '6b78e3f59faec1a4750acff08030a79b', '333', '333', NULL, NULL, 1, 'admin', '2019-02-21 19:59:47', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('7050c1522702bac3be40e3b7d2e1dfd8', 'c5a14c75172783d72cbee6ee7f5df5d1', '柱状图', 'bar', NULL, 1, 1, 'admin', '2019-04-12 17:05:17', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('71b924faa93805c5c1579f12e001c809', 'd6e1152968b02d69ff358c75b48a6ee1', 'OA办公', 'oa', NULL, 2, 1, 'admin', '2021-03-22 19:27:17', 'admin', '2019-03-22 23:24:36');
INSERT INTO `sys_dict_item` VALUES ('75b260d7db45a39fc7f21badeabdb0ed', 'c36169beb12de8a71c8683ee7c28a503', '不启用', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:29:41', 'admin', '2019-03-18 23:29:54');
INSERT INTO `sys_dict_item` VALUES ('7688469db4a3eba61e6e35578dc7c2e5', 'c36169beb12de8a71c8683ee7c28a503', '启用', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:29:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('78ea6cadac457967a4b1c4eb7aaa418c', 'fc6cd58fde2e8481db10d3a1e68ce70c', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:30:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('7ccf7b80c70ee002eceb3116854b75cb', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '按钮权限', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:25:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('81fb2bb0e838dc68b43f96cc309f8257', 'fc6cd58fde2e8481db10d3a1e68ce70c', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:30:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('83250269359855501ec4e9c0b7e21596', '4274efc2292239b6f000b153f50823ff', '可见/可访问(授权后可见/可访问)', '1', NULL, 1, 1, 'admin', '2019-05-10 17:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('84778d7e928bc843ad4756db1322301f', '4e4602b3e3686f0911384e188dc7efb4', '大于等于', '>=', '大于等于', 5, 1, 'admin', '2019-04-01 10:46:02', 'admin', '2019-04-01 17:49:05');
INSERT INTO `sys_dict_item` VALUES ('848d4da35ebd93782029c57b103e5b36', 'c5a14c75172783d72cbee6ee7f5df5d1', '饼图', 'pie', NULL, 3, 1, 'admin', '2019-04-12 17:05:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('84dfc178dd61b95a72900fcdd624c471', '78bda155fe380b1b3f175f1e88c284c6', '处理中', '2', '处理中', 2, 1, 'admin', '2019-05-09 16:33:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('86f19c7e0a73a0bae451021ac05b99dd', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '子菜单', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:25:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8bccb963e1cd9e8d42482c54cc609ca2', '4f69be5f507accea8d5df5f11346181a', '微信', '3', NULL, 3, 1, 'admin', '2021-05-11 14:29:12', 'admin', '2019-04-11 14:29:31');
INSERT INTO `sys_dict_item` VALUES ('8c618902365ca681ebbbe1e28f11a548', '4c753b5293304e7a445fd2741b46529d', '启用', '1', NULL, 0, 1, 'admin', '2020-07-18 23:19:27', 'admin', '2019-05-17 14:51:18');
INSERT INTO `sys_dict_item` VALUES ('8cdf08045056671efd10677b8456c999', '4274efc2292239b6f000b153f50823ff', '可编辑(未授权时禁用)', '2', NULL, 2, 1, 'admin', '2019-05-10 17:55:38', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8ff48e657a7c5090d4f2a59b37d1b878', '4d7fec1a7799a436d26d02325eff295e', '中', 'M', '中', 2, 1, 'admin', '2019-04-16 17:04:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('948923658baa330319e59b2213cda97c', '880a895c98afeca9d9ac39f29e67c13e', '添加', '2', NULL, 2, 1, 'admin', '2019-07-22 10:54:59', 'admin', '2019-07-22 10:55:36');
INSERT INTO `sys_dict_item` VALUES ('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', '4c753b5293304e7a445fd2741b46529d', '不启用', '0', NULL, 1, 1, 'admin', '2019-03-18 23:19:53', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a1e7d1ca507cff4a480c8caba7c1339e', '880a895c98afeca9d9ac39f29e67c13e', '导出', '6', NULL, 6, 1, 'admin', '2019-07-22 12:06:50', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a2be752dd4ec980afaec1efd1fb589af', '8dfe32e2d29ea9430a988b3b558bf233', '已撤销', '2', '已撤销', 3, 1, 'admin', '2019-04-16 17:41:39', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('aa0d8a8042a18715a17f0a888d360aa4', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '一级菜单', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:24:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('adcf2a1fe93bb99a84833043f475fe0b', '4e4602b3e3686f0911384e188dc7efb4', '包含', 'IN', '包含', 8, 1, 'admin', '2019-04-01 16:45:47', 'admin', '2019-04-01 17:49:24');
INSERT INTO `sys_dict_item` VALUES ('b029a41a851465332ee4ee69dcf0a4c2', '0b5d19e1fce4b2e6647e6b4a17760c14', '系统消息', '2', NULL, 1, 1, 'admin', '2019-02-22 18:02:08', 'admin', '2019-04-22 18:02:13');
INSERT INTO `sys_dict_item` VALUES ('b2a8b4bb2c8e66c2c4b1bb086337f393', '3486f32803bb953e7155dab3513dc68b', '正常', '0', NULL, NULL, 1, 'admin', '2022-10-18 21:46:48', 'admin', '2019-03-28 22:22:20');
INSERT INTO `sys_dict_item` VALUES ('b57f98b88363188daf38d42f25991956', '6b78e3f59faec1a4750acff08030a79b', '22', '222', NULL, NULL, 0, 'admin', '2019-02-21 19:59:43', 'admin', '2019-03-11 21:23:27');
INSERT INTO `sys_dict_item` VALUES ('b5f3bd5f66bb9a83fecd89228c0d93d1', '68168534ff5065a152bfab275c2136f8', '无效', '0', '无效', 1, 1, 'admin', '2019-04-26 19:21:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('b9fbe2a3602d4a27b45c100ac5328484', '78bda155fe380b1b3f175f1e88c284c6', '待提交', '1', '待提交', 1, 1, 'admin', '2019-05-09 16:32:35', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('ba27737829c6e0e582e334832703d75e', '236e8a4baff0db8c62c00dd95632834f', '同步', '1', '同步', 1, 1, 'admin', '2019-05-15 15:28:15', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('bcec04526b04307e24a005d6dcd27fd6', '880a895c98afeca9d9ac39f29e67c13e', '导入', '5', NULL, 5, 1, 'admin', '2019-07-22 12:06:41', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c53da022b9912e0aed691bbec3c78473', '880a895c98afeca9d9ac39f29e67c13e', '查询', '1', NULL, 1, 1, 'admin', '2019-07-22 10:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c5700a71ad08994d18ad1dacc37a71a9', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '否', '0', NULL, 1, 1, 'admin', '2019-05-22 19:29:55', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('cbfcc5b88fc3a90975df23ffc8cbe29c', 'c5a14c75172783d72cbee6ee7f5df5d1', '曲线图', 'line', NULL, 2, 1, 'admin', '2019-05-12 17:05:30', 'admin', '2019-04-12 17:06:06');
INSERT INTO `sys_dict_item` VALUES ('d217592908ea3e00ff986ce97f24fb98', 'c5a14c75172783d72cbee6ee7f5df5d1', '数据列表', 'table', NULL, 4, 1, 'admin', '2019-04-12 17:05:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('df168368dcef46cade2aadd80100d8aa', '3d9a351be3436fbefb1307d4cfb49bf2', '男', '1', NULL, 1, 1, NULL, '2027-08-04 14:56:49', 'admin', '2019-03-23 22:44:44');
INSERT INTO `sys_dict_item` VALUES ('e6329e3a66a003819e2eb830b0ca2ea0', '4e4602b3e3686f0911384e188dc7efb4', '小于', '<', '小于', 2, 1, 'admin', '2019-04-01 16:44:15', 'admin', '2019-04-01 17:48:34');
INSERT INTO `sys_dict_item` VALUES ('e94eb7af89f1dbfa0d823580a7a6e66a', '236e8a4baff0db8c62c00dd95632834f', '不同步', '0', '不同步', 2, 1, 'admin', '2019-05-15 15:28:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f0162f4cc572c9273f3e26b2b4d8c082', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'booostrap', '1', NULL, 1, 1, 'admin', '2021-08-22 16:32:04', 'admin', '2019-03-22 16:33:57');

-- ----------------------------
-- Table structure for sys_fill_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_fill_rule`;
CREATE TABLE `sys_fill_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则实现类',
  `rule_params` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则参数',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  UNIQUE INDEX `uk_sfr_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_fill_rule
-- ----------------------------
INSERT INTO `sys_fill_rule` VALUES ('1202551334738382850', '机构编码生成', 'org_num_role', 'org.jeecg.modules.system.rule.OrgCodeRule', '{\"parentId\":\"c6d7cb4deeac411cb3384b1b31278596\"}', 'admin', '2019-12-09 10:37:06', 'admin', '2019-12-05 19:32:35');
INSERT INTO `sys_fill_rule` VALUES ('1202787623203065858', '分类字典编码生成', 'category_code_rule', 'org.jeecg.modules.system.rule.CategoryCodeRule', '{\"pid\":\"\"}', 'admin', '2019-12-09 10:36:54', 'admin', '2019-12-06 11:11:31');
INSERT INTO `sys_fill_rule` VALUES ('1260134137920090113', '订单流水号', 'shop_order_num', 'org.jeecg.modules.online.cgform.rule.OrderNumberRule', '{}', 'admin', '2020-12-07 18:29:50', 'admin', '2020-05-12 17:06:05');

-- ----------------------------
-- Table structure for sys_gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_gateway_route`;
CREATE TABLE `sys_gateway_route`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `router_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名',
  `uri` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务地址',
  `predicates` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '断言',
  `filters` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '过滤器',
  `retryable` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否重试:0-否 1-是',
  `strip_prefix` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否忽略前缀0-否 1-是',
  `persistable` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否为保留数据:0-否 1-是',
  `show_api` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否在接口文档中展示:0-否 1-是',
  `status` decimal(11, 0) NULL DEFAULT NULL COMMENT '状态:0-无效 1-有效',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_gateway_route
-- ----------------------------
INSERT INTO `sys_gateway_route` VALUES ('1331051599401857026', 'jeecg-demo-websocket', 'jeecg-demo-websocket', 'lb:ws://jeecg-demo', '[{\"args\":[\"/vxeSocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-24 09:46:46', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('jeecg-cloud-websocket', 'jeecg-system-websocket', 'jeecg-system-websocket', 'lb:ws://jeecg-system', '[{\"args\":[\"/websocket/**\",\"/eoaSocket/**\",\"/newsWebsocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('jeecg-demo', 'jeecg-demo', 'jeecg-demo', 'lb://jeecg-demo', '[{\"args\":[\"/mock/**\",\"/test/**\",\"/bigscreen/template1/**\",\"/bigscreen/template2/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route` VALUES ('jeecg-system', 'jeecg-system', 'jeecg-system', 'lb://jeecg-system', '[{\"args\":[\"/sys/**\",\"/eoa/**\",\"/joa/**\",\"/online/**\",\"/bigscreen/**\",\"/jmreport/**\",\"/desform/**\",\"/process/**\",\"/act/**\",\"/plug-in/***/\",\"/druid/**\",\"/generic/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `log_type` decimal(11, 0) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）',
  `log_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志内容',
  `operate_type` decimal(11, 0) NULL DEFAULT NULL COMMENT '操作类型',
  `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户账号',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求java方法',
  `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `request_param` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型',
  `cost_time` decimal(20, 0) NULL DEFAULT NULL COMMENT '耗时',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  INDEX `idx_sl_create_time`(`create_time`) USING BTREE,
  INDEX `idx_sl_log_type`(`log_type`) USING BTREE,
  INDEX `idx_sl_operate_type`(`operate_type`) USING BTREE,
  INDEX `idx_sl_userid`(`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1670316130794528769', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-18 14:22:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1670324216057880577', 2, 'km_doc-文件上传', 1, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmDocController.uploadDoc()', NULL, '[null,null]', NULL, 133, NULL, '2023-06-18 14:55:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1670324591968182274', 2, 'km_doc-文件上传', 1, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmDocController.uploadDoc()', NULL, '[null,null]', NULL, 30796, NULL, '2023-06-18 14:56:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1670326409272037378', 2, 'km_doc-文件上传', 1, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmDocController.uploadDoc()', NULL, '[null,null]', NULL, 278, NULL, '2023-06-18 15:03:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1670326745558757378', 2, 'km_doc-通过id删除', 4, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmDocController.delete()', NULL, '  id: 402881ec88cd51160188cd5116db0001  req: org.apache.shiro.web.servlet.ShiroHttpServletRequest@41a8f62e', NULL, 660, NULL, '2023-06-18 15:05:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1670326769520816130', 2, 'km_doc-文件上传', 1, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmDocController.uploadDoc()', NULL, '[null,null]', NULL, 189, NULL, '2023-06-18 15:05:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1672124946553057282', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2023-06-23 14:10:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1672125012772728834', 2, 'km_sys_config-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.KM.controller.KmSysConfigController.queryPageList()', NULL, '  kmSysConfig: KmSysConfig(id=null, itemCode=null, itemValue=null, itemName=null)  pageNo: 1  pageSize: 10  req: org.apache.shiro.web.servlet.ShiroHttpServletRequest@465d65af', NULL, 58, NULL, '2023-06-23 14:10:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1672125052597645314', 2, 'km_sys_config-分页列表查询', 1, 'admin', '管理员', '0:0:0:0:0:0:0:1', 'org.jeecg.modules.KM.controller.KmSysConfigController.queryPageList()', NULL, '  kmSysConfig: KmSysConfig(id=null, itemCode=null, itemValue=null, itemName=null)  pageNo: 2  pageSize: 10  req: org.apache.shiro.web.servlet.ShiroHttpServletRequest@526e73d7', NULL, 40, NULL, '2023-06-23 14:10:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1672125062345207810', 2, 'km_sys_config-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.KM.controller.KmSysConfigController.queryPageList()', NULL, '  kmSysConfig: KmSysConfig(id=null, itemCode=null, itemValue=null, itemName=null)  pageNo: 1  pageSize: 10  req: org.apache.shiro.web.servlet.ShiroHttpServletRequest@bee47a0', NULL, 38, NULL, '2023-06-23 14:10:56', NULL, NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名字',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级菜单跳转地址',
  `menu_type` decimal(11, 0) NULL DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单权限编码',
  `perms_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限策略1显示2禁用',
  `sort_no` decimal(8, 2) NULL DEFAULT NULL COMMENT '菜单排序',
  `always_show` decimal(4, 0) NULL DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_route` decimal(4, 0) NULL DEFAULT NULL COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` decimal(4, 0) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `keep_alive` decimal(4, 0) NULL DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否隐藏路由: 0否,1是',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` decimal(11, 0) NULL DEFAULT NULL COMMENT '删除状态 0正常 1已删除',
  `rule_flag` decimal(11, 0) NULL DEFAULT NULL COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` decimal(4, 0) NULL DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', NULL, '首页', '/dashboard/analysis', 'dashboard/Analysis', NULL, NULL, 0, NULL, NULL, 0.00, 0, 'home', 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-29 11:04:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('9cb91b8851db0cf7b19d7ecc2a8193dd', '1939e035e803a99ceecb6f5563570fb2', '我的任务表单', '/modules/bpm/task/form/FormModule', 'modules/bpm/task/form/FormModule', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-08 16:49:05', 'admin', '2019-03-08 18:37:56', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('b6bcee2ccc854052d3cc3e9c96d90197', '71102b3b87fb07e5527bbd2c530dd90a', '加班申请', '/modules/extbpm/joa/JoaOvertimeList', 'modules/extbpm/joa/JoaOvertimeList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-03 15:33:10', 'admin', '2019-04-03 15:34:48', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', NULL, '系统管理', '/isystem', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 7.00, 0, 'setting', 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2021-07-14 01:50:27', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('1415139394194399233', NULL, '文件管理', '/km/filemanagement', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 1.00, 0, 'folder', 1, 0, 0, 0, NULL, 'admin', '2021-07-13 21:41:38', 'admin', '2021-07-14 01:40:06', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1415145872468893697', '1415139394194399233', '知识文件管理', '/km/filemanagement/draftslist', 'km/filemanagement/DraftsList', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-07-13 22:07:23', 'admin', '2022-03-28 00:11:34', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1415156984451063809', NULL, '配置管理', '/km/datasetting', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 2.00, 0, 'cluster', 1, 0, 0, 0, NULL, 'admin', '2021-07-13 22:51:32', 'admin', '2021-07-29 17:28:49', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1415277817119023106', '1415156984451063809', '文档属性定义', '/km/datasetting/dictlist', 'km/datasetting/DictList', NULL, NULL, 1, NULL, '1', NULL, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-07-14 06:51:41', 'admin', '2021-07-29 17:28:22', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1419574496959193090', '1415156984451063809', '系统参数', '/km/datasetting/KmSysConfigList', 'km/datasetting/KmSysConfigList', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-07-26 03:25:09', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1420569665158332418', NULL, '日志', '/layouts/RouteView', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 1.80, 0, 'diff', 1, 0, 0, 0, NULL, 'admin', '2021-07-29 10:19:36', 'admin', '2021-08-04 17:04:21', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1422846497589129218', '1415139394194399233', '文件统计', '/km/filemanagement/FileStatisticsList', 'km/filemanagement/FileStatisticsList', NULL, NULL, 1, NULL, '1', 4.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2021-08-04 17:06:55', 'ceshi', '2021-10-14 18:01:35', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('190c2b43bec6a5f7a4194a85db67d96a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/roleUserList', 'system/RoleUserList', NULL, NULL, 1, NULL, NULL, 1.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:13:56', 'admin', '2019-12-25 09:36:31', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', NULL, NULL, 1, NULL, NULL, 1.10, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:24', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/isystem/depart', 'system/DepartList', NULL, NULL, 1, NULL, NULL, 1.40, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-29 18:47:40', 'admin', '2019-12-25 09:36:47', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', NULL, NULL, 1, NULL, NULL, 1.30, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:39', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('58857ff846e61794c69208e9d3a85466', '1420569665158332418', '系统日志', '/isystem/log', 'system/LogList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-26 10:11:18', 'admin', '2021-07-29 10:20:23', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('841057b8a1bef8f6b4b20f9a618a7fa6', '1420569665158332418', '数据日志', '/sys/dataLog-list', 'system/DataLogList', NULL, NULL, 1, NULL, NULL, 2.10, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-03-11 19:26:49', 'admin', '2023-06-23 14:11:38', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('1453933027082489858', '1415139394194399233', '收藏夹', '/km/filemanagement/KmDocFavouriteList', 'km/filemanagement/KmDocFavouriteList', NULL, NULL, 1, NULL, '1', 0.00, 0, NULL, 1, 1, 0, 0, NULL, 'ceshi', '2021-10-29 11:53:41', NULL, NULL, 0, NULL, '1', 0);
INSERT INTO `sys_permission` VALUES ('1456909004028821505', '1415139394194399233', '检索列表', '/km/search/DoSearch', 'km/search/DoSearch', NULL, NULL, 1, NULL, '1', 9.00, 0, NULL, 1, 0, 0, 1, NULL, 'ceshi', '2021-11-06 16:59:09', NULL, NULL, 0, NULL, '1', 0);
INSERT INTO `sys_permission` VALUES ('1456909292378832898', '1456909004028821505', '批量下载', '/km/search/DoSearch', NULL, NULL, NULL, 2, 'searchList:batchDownload', '1', NULL, 0, NULL, 1, 1, 0, 0, NULL, 'ceshi', '2021-11-06 17:00:18', NULL, NULL, 0, NULL, '1', 0);

-- ----------------------------
-- Table structure for sys_permission_data_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_data_rule`;
CREATE TABLE `sys_permission_data_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
  `rule_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段',
  `rule_conditions` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件',
  `rule_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '规则值',
  `status` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限有效状态1有0否',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission_data_rule
-- ----------------------------
INSERT INTO `sys_permission_data_rule` VALUES ('40283181614231d401614234fe670003', '40283181614231d401614232cd1c0001', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', '2018-01-29 21:57:04', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('4028318161424e730161424fca6f0004', '4028318161424e730161424f61510002', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', '2018-01-29 22:26:20', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880e6487e661a01487e732c020005', '402889fb486e848101486e93a7c80014', 'SYS_ORG_CODE', 'SYS_ORG_CODE', 'LIKE', '010201%', '1', '2014-09-16 20:32:30', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880e6487e661a01487e8153ee0007', '402889fb486e848101486e93a7c80014', 'create_by', 'create_by', NULL, '#{SYS_USER_CODE}', '1', '2014-09-16 20:47:57', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880ec5ddec439015ddf9225060038', '40288088481d019401481d2fcebf000d', '复杂关系', NULL, 'USE_SQL_RULES', 'name like \'%张%\' or age > 10', '1', NULL, NULL, '2017-08-14 15:10:25', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402880ec5ddfdd26015ddfe3e0570011', '4028ab775dca0d1b015dca3fccb60016', '复杂sql配置', NULL, 'USE_SQL_RULES', 'table_name like \'%test%\' or is_tree = \'Y\'', '1', NULL, NULL, '2017-08-14 16:38:55', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402880f25b1e2ac7015b1e5fdebc0012', '402880f25b1e2ac7015b1e5cdc340010', '只能看自己数据', 'create_by', '=', '#{sys_user_code}', '1', '2017-03-30 16:40:51', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881875b19f141015b19f8125e0014', '40288088481d019401481d2fcebf000d', '可看下属业务数据', 'sys_org_code', 'LIKE', '#{sys_org_code}', '1', NULL, NULL, '2017-08-14 15:04:32', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402881e45394d66901539500a4450001', '402881e54df73c73014df75ab670000f', 'sysCompanyCode', 'sysCompanyCode', '=', '#{SYS_COMPANY_CODE}', '1', '2016-03-21 01:09:21', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e45394d6690153950177cb0003', '402881e54df73c73014df75ab670000f', 'sysOrgCode', 'sysOrgCode', '=', '#{SYS_ORG_CODE}', '1', '2016-03-21 01:10:15', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f43101626727aff60067', '402881e56266f43101626724eb730065', '销售自己看自己的数据', 'createBy', '=', '#{sys_user_code}', '1', '2018-03-27 19:11:16', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f4310162672fb1a70082', '402881e56266f43101626724eb730065', '销售经理看所有下级数据', 'sysOrgCode', 'LIKE', '#{sys_org_code}', '1', '2018-03-27 19:20:01', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f431016267387c9f0088', '402881e56266f43101626724eb730065', '只看金额大于1000的数据', 'money', '>=', '1000', '1', '2018-03-27 19:29:37', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881f3650de25101650dfb5a3a0010', '402881e56266f4310162671d62050044', '22', NULL, 'USE_SQL_RULES', '22', '1', '2018-08-06 14:45:01', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402889fb486e848101486e913cd6000b', '402889fb486e848101486e8e2e8b0007', 'userName', 'userName', '=', 'admin', '1', '2014-09-13 18:31:25', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402889fb486e848101486e98d20d0016', '402889fb486e848101486e93a7c80014', 'title', 'title', '=', '12', '1', NULL, NULL, '2014-09-13 22:18:22', 'scott');
INSERT INTO `sys_permission_data_rule` VALUES ('402889fe47fcb29c0147fcb6b6220001', '8a8ab0b246dc81120146dc8180fe002b', '12', '12', '>', '12', '1', '2014-08-22 15:55:38', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('4028ab775dca0d1b015dca4183530018', '4028ab775dca0d1b015dca3fccb60016', '表名限制', 'isDbSynch', '=', 'Y', '1', NULL, NULL, '2017-08-14 16:43:45', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('4028ef815595a881015595b0ccb60001', '40288088481d019401481d2fcebf000d', '限只能看自己', 'create_by', '=', '#{sys_user_code}', '1', NULL, NULL, '2017-08-14 15:03:56', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('4028ef81574ae99701574aed26530005', '4028ef81574ae99701574aeb97bd0003', '用户名', 'userName', '!=', 'admin', '1', '2016-09-21 12:07:18', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('f852d85d47f224990147f2284c0c0005', NULL, '小于', 'test', '<=', '11', '1', '2014-08-20 14:43:52', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构编码',
  UNIQUE INDEX `uniq_position_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_position
-- ----------------------------
INSERT INTO `sys_position` VALUES ('1185040064792571906', 'devleader', '研发部经理', '2', NULL, 'admin', '2019-10-18 11:49:03', 'admin', '2020-02-23 22:55:42', 'A01');
INSERT INTO `sys_position` VALUES ('1256485574212153345', '总经理', 'laozong', '5', NULL, 'admin', '2020-05-02 15:28:00', 'admin', '2020-05-02 15:28:03', '北京国炬公司');

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` decimal(11, 0) NULL DEFAULT NULL COMMENT '删除状态',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `job_class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类名',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `parameter` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` decimal(11, 0) NULL DEFAULT NULL COMMENT '状态 0正常 -1停止',
  UNIQUE INDEX `uniq_job_class_name`(`job_class_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO `sys_quartz_job` VALUES ('df26ecacf0f75d219d746750fe84bbee', NULL, NULL, 0, 'admin', '2020-05-02 15:40:35', 'org.jeecg.modules.quartz.job.SampleParamJob', '0/1 * * * * ?', 'scott', '带参测试 后台将每隔1秒执行输出日志', -1);
INSERT INTO `sys_quartz_job` VALUES ('a253cdfc811d69fa0efc70d052bc8128', 'admin', '2019-03-30 12:44:48', 0, 'admin', '2020-05-02 15:48:49', 'org.jeecg.modules.quartz.job.SampleJob', '0/1 * * * * ?', NULL, NULL, -1);
INSERT INTO `sys_quartz_job` VALUES ('5b3d2c087ad41aa755fc4f89697b01e7', 'admin', '2019-04-11 19:04:21', 0, 'admin', '2020-05-02 15:48:48', 'org.jeecg.modules.message.job.SendMsgJob', '0/50 * * * * ? *', NULL, NULL, -1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', NULL, '2018-12-21 18:03:39', 'admin', '2022-01-06 14:36:13');
INSERT INTO `sys_role` VALUES ('1501891096828817410', 'test', '9dcb6f3da512450db97043164d6894e6', NULL, 'ceshi', '2022-03-10 20:01:56', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据权限ids',
  `operate_date` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operate_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作ip'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1e47db875601fd97723254046b5bba90', 'f6817f48af4fb3af11b9e8bf182f618b', 'baf16b7174bd821b6bab23fa9abb200d', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('2779cdea8367fff37db26a42c1a1f531', 'f6817f48af4fb3af11b9e8bf182f618b', 'fef097f3903caf3a3c3a6efa8de43fbb', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('3de2a60c7e42a521fecf6fcc5cb54978', 'f6817f48af4fb3af11b9e8bf182f618b', '2d83d62bd2544b8994c8f38cf17b0ddf', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('6daddafacd7eccb91309530c17c5855d', 'f6817f48af4fb3af11b9e8bf182f618b', 'edfa74d66e8ea63ea432c2910837b150', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('7413acf23b56c906aedb5a36fb75bd3a', 'f6817f48af4fb3af11b9e8bf182f618b', 'a4fc7b64b01a224da066bb16230f9c5a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4dab5a06acc8ef3297889872caa74747', 'f6817f48af4fb3af11b9e8bf182f618b', 'ffb423d25cc59dcd0532213c4a518261', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('4f254549d9498f06f4cc9b23f3e2c070', 'f6817f48af4fb3af11b9e8bf182f618b', '93d5cfb4448f11e9916698e7f462b4b6', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('504e326de3f03562cdd186748b48a8c7', 'f6817f48af4fb3af11b9e8bf182f618b', '027aee69baee98a0ed2e01806e89c891', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('5de6871fadb4fe1cdd28989da0126b07', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1269526122208522241', 'f6817f48af4fb3af11b9e8bf182f618b', '1267412134208319489', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1415139448871346178', 'f6817f48af4fb3af11b9e8bf182f618b', '1415139394194399233', NULL, '2021-07-13 21:41:51', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1415145981671792642', 'f6817f48af4fb3af11b9e8bf182f618b', '1415145872468893697', NULL, '2021-07-13 22:07:49', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1415157254522298370', 'f6817f48af4fb3af11b9e8bf182f618b', '1415156984451063809', NULL, '2021-07-13 22:52:36', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1415277853504610305', 'f6817f48af4fb3af11b9e8bf182f618b', '1415277817119023106', NULL, '2021-07-14 06:51:50', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1419574538910621698', 'f6817f48af4fb3af11b9e8bf182f618b', '1419574496959193090', NULL, '2021-07-26 03:25:19', '192.168.0.103');
INSERT INTO `sys_role_permission` VALUES ('1420570298724728833', 'f6817f48af4fb3af11b9e8bf182f618b', '1420569665158332418', NULL, '2021-07-29 10:22:07', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1422846634373771266', 'f6817f48af4fb3af11b9e8bf182f618b', '1422846497589129218', NULL, '2021-08-04 17:07:27', '192.168.0.104');
INSERT INTO `sys_role_permission` VALUES ('cf1feb1bf69eafc982295ad6c9c8d698', 'f6817f48af4fb3af11b9e8bf182f618b', 'a2b11669e98c5fe54a53c3e3c4f35d14', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dbc5dd836d45c5bc7bc94b22596ab956', 'f6817f48af4fb3af11b9e8bf182f618b', '1939e035e803a99ceecb6f5563570fb2', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dc83bb13c0e8c930e79d28b2db26f01f', 'f6817f48af4fb3af11b9e8bf182f618b', '63b551e81c5956d5c861593d366d8c57', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('dc8fd3f79bd85bd832608b42167a1c71', 'f6817f48af4fb3af11b9e8bf182f618b', '91c23960fab49335831cf43d820b0a61', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('de82e89b8b60a3ea99be5348f565c240', 'f6817f48af4fb3af11b9e8bf182f618b', '56ca78fe0f22d815fabc793461af67b8', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('ec846a3f85fdb6813e515be71f11b331', 'f6817f48af4fb3af11b9e8bf182f618b', '732d48f8e0abe99fe6a23d18a3171cd1', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('f177acac0276329dc66af0c9ad30558a', 'f6817f48af4fb3af11b9e8bf182f618b', 'c2c356bf4ddd29975347a7047a062440', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('fafe73c4448b977fe42880a6750c3ee8', 'f6817f48af4fb3af11b9e8bf182f618b', '9cb91b8851db0cf7b19d7ecc2a8193dd', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1453933071680524290', 'f6817f48af4fb3af11b9e8bf182f618b', '1453933027082489858', NULL, '2021-10-29 11:53:51', '192.168.0.102');
INSERT INTO `sys_role_permission` VALUES ('84eac2f113c23737128fb099d1d1da89', 'f6817f48af4fb3af11b9e8bf182f618b', '03dc3d93261dda19fc86dd7ca486c6cf', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('86060e2867a5049d8a80d9fe5d8bc28b', 'f6817f48af4fb3af11b9e8bf182f618b', '765dd244f37b804e3d00f475fd56149b', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1458355149641641985', 'f6817f48af4fb3af11b9e8bf182f618b', '1456909292378832898', NULL, '2021-11-10 16:45:37', '116.23.216.197');
INSERT INTO `sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('a034ed7c38c996b880d3e78f586fe0ae', 'f6817f48af4fb3af11b9e8bf182f618b', 'c89018ea6286e852b424466fd92a2ffc', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('acacce4417e5d7f96a9c3be2ded5b4be', 'f6817f48af4fb3af11b9e8bf182f618b', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('af60ac8fafd807ed6b6b354613b9ccbc', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', NULL, NULL, NULL);
INSERT INTO `sys_role_permission` VALUES ('1479012049120796674', 'f6817f48af4fb3af11b9e8bf182f618b', '1456909004028821505', NULL, '2022-01-06 16:48:46', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` VALUES ('1501891384792952834', '1501891096828817410', '9502685863ab87f0ad1134142788a385', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384809730049', '1501891096828817410', '1415139394194399233', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384809730050', '1501891096828817410', '1453933027082489858', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384809730052', '1501891096828817410', '1415145872468893697', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701569', '1501891096828817410', '1422846497589129218', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701570', '1501891096828817410', '1456909004028821505', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701571', '1501891096828817410', '1456909292378832898', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701572', '1501891096828817410', '1420569665158332418', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701574', '1501891096828817410', '58857ff846e61794c69208e9d3a85466', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701575', '1501891096828817410', '1415156984451063809', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701576', '1501891096828817410', '1415277817119023106', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1501891384830701578', '1501891096828817410', '1419574496959193090', NULL, '2022-03-10 20:03:05', '116.23.218.77');
INSERT INTO `sys_role_permission` VALUES ('1672125560288784385', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', NULL, '2023-06-23 14:12:55', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` decimal(11, 0) NOT NULL COMMENT '租户编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `status` decimal(11, 0) NULL DEFAULT NULL COMMENT '状态 1正常 0冻结'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '多租户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1, '北京租户001', '2020-07-10 15:43:32', 'admin', NULL, NULL, 1);

-- ----------------------------
-- Table structure for sys_third_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_third_account`;
CREATE TABLE `sys_third_account`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `sys_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方登录id',
  `third_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录来源',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `status` decimal(4, 0) NULL DEFAULT NULL COMMENT '状态(1-正常,2-冻结)',
  `del_flag` decimal(4, 0) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `third_user_uuid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方账号',
  `third_user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方app用户账号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` decimal(4, 0) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `status` decimal(4, 0) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` decimal(4, 0) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `third_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方登录的唯一标识',
  `third_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方类型',
  `activiti_sync` decimal(4, 0) NULL DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
  `work_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
  `post` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机号',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_identity` decimal(4, 0) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
  `depart_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '负责部门',
  `rel_tenant_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多租户标识',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', '', '2018-12-05 00:00:00', 1, 'jeecg@163.com', '18611111111', 'A01', 1, 0, NULL, NULL, 1, '00001', '总经理', NULL, NULL, '2019-06-21 17:54:10', 'admin', '2021-10-09 21:03:21', 2, 'c6d7cb4deeac411cb3384b1b31278596', NULL, NULL);
INSERT INTO `sys_user` VALUES ('1448560127207559169', 'ceshi', '测试', '8480d522fcf8d62c', 'bslhULTz', NULL, NULL, NULL, NULL, NULL, 'A01', 1, 0, 'bf4ff455bd3344e784cf2ba92ffee2e1', '1', NULL, NULL, NULL, NULL, NULL, '2021-10-14 16:03:41', 'ceshi', '2022-03-22 14:12:52', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_agent
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_agent`;
CREATE TABLE `sys_user_agent`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '序号',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `agent_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理人用户名',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '代理开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '代理结束时间',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态0无效1有效',
  `create_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `sys_company_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属公司',
  UNIQUE INDEX `uk_sug_user_name`(`user_name`) USING BTREE,
  INDEX `idx_sug_end_time`(`end_time`) USING BTREE,
  INDEX `idx_sug_start_time`(`start_time`) USING BTREE,
  INDEX `idx_sug_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户代理人设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart`;
CREATE TABLE `sys_user_depart`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `dep_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------
INSERT INTO `sys_user_depart` VALUES ('1506108597582270465', '1448560127207559169', 'f79b7d5a99b1442c876858a6961cb1fb');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1446823599141052418', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('1446823599141052418', 'f6817f48af4fb3af11b9e8bf182f618b', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('1506108597502578689', '1448560127207559169', '1501891096828817410');

SET FOREIGN_KEY_CHECKS = 1;
