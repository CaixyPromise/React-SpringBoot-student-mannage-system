# 数据库初始化

-- 创建库
create database if not exists student_system;

-- 切换库
use student_system;
/*
 Navicat Premium Data Transfer

 Source Server         : localMySql
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : localhost:3306
 Source Schema         : student_system

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 29/04/2024 16:21:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for classes_info
-- ----------------------------
DROP TABLE IF EXISTS `classes_info`;
CREATE TABLE `classes_info`  (
                                 `id` bigint(20) NOT NULL COMMENT '主键Id',
                                 `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级名称',
                                 `departId` bigint(20) NOT NULL COMMENT '学院id',
                                 `majorId` bigint(20) NOT NULL COMMENT '专业id',
                                 `creatorId` bigint(20) NOT NULL COMMENT '创建人Id',
                                 `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                 `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除键',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `classes_info_isDelete_index`(`isDelete`) USING BTREE,
                                 INDEX `classes_info_majorId_departId_index`(`majorId`, `departId`) USING BTREE,
                                 INDEX `classes_info_name_index`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classes_info
-- ----------------------------
INSERT INTO `classes_info` VALUES (1784857522595704834, '20电子商务2班', 1771929790011404289, 1771897685382525921, 1744359873585348610, '2024-04-29 16:09:29', '2024-04-29 16:09:29', 0);
INSERT INTO `classes_info` VALUES (1784858921022795777, '20电子商务1班', 1771929790011404289, 1771897685382525921, 1744359873585348610, '2024-04-29 16:15:02', '2024-04-29 16:15:02', 0);

-- ----------------------------
-- Table structure for department_info
-- ----------------------------
DROP TABLE IF EXISTS `department_info`;
CREATE TABLE `department_info`  (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院名称',
                                    `createUserId` bigint(20) NOT NULL COMMENT '添加该学院的用户id',
                                    `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                    `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1771936521391169539 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学院信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department_info
-- ----------------------------
INSERT INTO `department_info` VALUES (1771897164336721921, '计算机学院', 1, '2024-03-24 21:49:38', '2024-03-24 21:49:38', 0);
INSERT INTO `department_info` VALUES (1771929790011404289, '信息管理与工程学院', 1, '2024-03-24 23:59:17', '2024-03-25 00:52:18', 0);
INSERT INTO `department_info` VALUES (1771936521391169538, '商务管理学院', 1, '2024-03-25 00:26:02', '2024-03-25 03:18:51', 0);

-- ----------------------------
-- Table structure for major_info
-- ----------------------------
DROP TABLE IF EXISTS `major_info`;
CREATE TABLE `major_info`  (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `departId` bigint(20) NOT NULL COMMENT '学院id',
                               `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院名称',
                               `createUserId` bigint(20) NOT NULL COMMENT '创建该目录用户id',
                               `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                               `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `idx_depart_id`(`departId`) USING BTREE,
                               INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1772162330286616578 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of major_info
-- ----------------------------
INSERT INTO `major_info` VALUES (1771897235031715841, 1771897164336721921, '软件工程', 1, '2024-03-24 21:49:55', '2024-03-24 21:49:55', 0);
INSERT INTO `major_info` VALUES (1771897271991922689, 1771897164336721921, '计算机科学与技术', 1, '2024-03-24 21:50:04', '2024-03-24 21:50:04', 0);
INSERT INTO `major_info` VALUES (1771897319966371842, 1771897164336721921, '人工智能', 1, '2024-03-24 21:50:16', '2024-03-24 21:50:16', 0);
INSERT INTO `major_info` VALUES (1771897685382524930, 1771897164336721921, '电子信息工程', 1, '2024-03-24 21:51:43', '2024-03-24 21:51:43', 0);
INSERT INTO `major_info` VALUES (1771897685382525921, 1771929790011404289, '电子商务', 1, '2024-03-25 00:17:36', '2024-03-25 15:20:46', 0);
INSERT INTO `major_info` VALUES (1771980734472257538, 1771897164336721921, '机器人工程', 1, '2024-03-25 03:21:43', '2024-03-25 03:21:43', 0);
INSERT INTO `major_info` VALUES (1772142280397811713, 1771936521391169538, '工商管理', 1, '2024-03-25 14:03:39', '2024-03-25 14:16:55', 1);

-- ----------------------------
-- Table structure for student_grades
-- ----------------------------
DROP TABLE IF EXISTS `student_grades`;
CREATE TABLE `student_grades`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   `stuId` bigint(20) NOT NULL COMMENT '学生ID，引用自student_score表',
                                   `subjectId` bigint(20) NOT NULL COMMENT '科目id',
                                   `grade` bigint(20) NOT NULL COMMENT '成绩',
                                   `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `student_grades_stuId_index`(`stuId`) USING BTREE,
                                   INDEX `student_grades_subjectId_index`(`subjectId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生成绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
                                 `id` bigint(20) NOT NULL COMMENT '学生ID',
                                 `stuName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生姓名',
                                 `stuSex` tinyint(11) NULL DEFAULT NULL COMMENT '学生性别',
                                 `stuDeptId` bigint(20) NOT NULL COMMENT '学生学院id',
                                 `stuMajorId` bigint(20) NOT NULL COMMENT '学生专业id',
                                 `stuClassId` bigint(20) NOT NULL COMMENT '学生班级Id',
                                 `creatorId` bigint(20) NOT NULL COMMENT '创建人Id',
                                 `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                 `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '是否删除逻辑键',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `student_info_stuName_index`(`stuName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for subjects
-- ----------------------------
DROP TABLE IF EXISTS `subjects`;
CREATE TABLE `subjects`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '科目名称',
                             `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                             `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `subjects_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `userAccount` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号(用户学号/工号)',
                         `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                         `userSex` int(11) NULL DEFAULT NULL COMMENT '用户',
                         `userEmail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户邮箱',
                         `userPhone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
                         `userDepartment` bigint(20) NULL DEFAULT NULL COMMENT '用户部门/院系id(学院)',
                         `userMajor` bigint(20) NULL DEFAULT NULL COMMENT '用户专业id',
                         `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
                         `userTags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户标签',
                         `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
                         `userRole` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
                         `userRoleLevel` int(11) NOT NULL DEFAULT 0 COMMENT '角色权限等级(细分角色权限)',
                         `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                         `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE INDEX `idx_user_account`(`userAccount`) USING BTREE,
                         INDEX `idx_user_userDepartment`(`userDepartment`) USING BTREE,
                         INDEX `idx_user_userMajor`(`userMajor`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'caixypromise', '$2a$10$AAA9ygpwNTqL7Dh1clVq5etmEBSdUF9s21.sloa0cHc7qTfRZkNXW', 1, '1944630344@qq.com', NULL, 1758426616761344002, 1758429386205749250, 'caixy', '[\"C++\", \"Python\", \"Java\", \"SpringBoot\"]', 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png', '这个网站的老大', 'admin', 0, '2024-02-08 17:15:07', '2024-03-02 19:03:14', 0);

SET FOREIGN_KEY_CHECKS = 1;
