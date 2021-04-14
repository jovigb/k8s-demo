/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50714
 Source Host           : localhost:3306
 Source Schema         : seckill-rabbitmq

 Target Server Type    : MySQL
 Target Server Version : 50714
 File Encoding         : 65001

 Date: 14/04/2021 17:43:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill`  (
  `seckill_id` bigint(20) NOT NULL COMMENT '商品库存id',
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `inventory` int(11) NOT NULL COMMENT '库存数量',
  `start_time` datetime(0) NOT NULL COMMENT '秒杀开启时间',
  `end_time` datetime(0) NOT NULL COMMENT '秒杀结束时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `version` bigint(20) NOT NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`seckill_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for td_order
-- ----------------------------
DROP TABLE IF EXISTS `td_order`;
CREATE TABLE `td_order`  (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT -1 COMMENT '状态标示:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功明细表' ROW_FORMAT = Dynamic;

truncate table td_order;
update seckill set inventory=100,version=0 where seckill_id=100001;

SET FOREIGN_KEY_CHECKS = 1;
