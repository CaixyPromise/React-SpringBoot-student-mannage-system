-- 创建库
create database if not exists student_system;
-- 切换库
use student_system;

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
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `classes_info_isDelete_index`(`isDelete`) USING BTREE,
  INDEX `classes_info_majorId_departId_index`(`majorId`, `departId`) USING BTREE,
  INDEX `classes_info_name_index`(`name`) USING BTREE,
  INDEX `idx_classes_info_id_isDelete`(`id`, `isDelete`) USING BTREE,
  INDEX `idx_classes_info_majorId_isDelete`(`majorId`, `isDelete`) USING BTREE,
  INDEX `idx_classes_info_departId_isDelete`(`departId`, `isDelete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for course_selection_classes
-- ----------------------------
DROP TABLE IF EXISTS `course_selection_classes`;
CREATE TABLE `course_selection_classes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `courseSelectionId` bigint(20) NOT NULL COMMENT '选课信息ID，关联course_selection_info表',
  `classId` bigint(20) NOT NULL COMMENT '班级ID，关联classes_info表',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_courseSelectionId`(`courseSelectionId`) USING BTREE,
  INDEX `idx_classId`(`classId`) USING BTREE,
  INDEX `idx_course_selection_classes_courseSelectionId_isDelete`(`courseSelectionId`, `isDelete`) USING BTREE,
  INDEX `idx_course_selection_classes_classId_isDelete`(`classId`, `isDelete`) USING BTREE,
  INDEX `idx_course_selection_classedId_courseSelectionId`(`classId`, `courseSelectionId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_selection_info
-- ----------------------------
DROP TABLE IF EXISTS `course_selection_info`;
CREATE TABLE `course_selection_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semesterId` bigint(20) NOT NULL COMMENT '学期ID，关联semesters表',
  `taskName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选课任务名称',
  `minCredit` double NOT NULL DEFAULT 1 COMMENT '选课最小学分',
  `startDate` datetime NOT NULL COMMENT '选课开始时间',
  `endDate` datetime NOT NULL COMMENT '选课结束时间',
  `creatorId` bigint(20) NOT NULL COMMENT '创建人ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isActive` tinyint(4) NOT NULL DEFAULT 1 COMMENT '任务状态',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_course_selection_semesterId`(`semesterId`) USING BTREE,
  INDEX `idx_course_selection_isDelete`(`isDelete`) USING BTREE,
  INDEX `idx_course_selection_semester_subject_isDelete`(`semesterId`, `isDelete`) USING BTREE,
  INDEX `idx_course_selection_start_end_date`(`startDate`, `endDate`) USING BTREE,
  INDEX `idx_course_selection_semester_isDelete_createTime`(`semesterId`, `isDelete`, `createTime`) USING BTREE,
  INDEX `idx_course_selection_subject_semester_isDelete_createTime`(`semesterId`, `isDelete`, `createTime`) USING BTREE,
  INDEX `idx_course_selection_start_end_isDelete`(`isDelete`, `startDate`, `endDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1903168783054372867 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '选课信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for course_selection_subject
-- ----------------------------
DROP TABLE IF EXISTS `course_selection_subject`;
CREATE TABLE `course_selection_subject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `courseSelectionId` bigint(20) NOT NULL COMMENT '选课信息ID，关联course_selection_info表',
  `subjectId` bigint(20) NOT NULL COMMENT '科目ID，关联subject表',
  `maxStudents` int(11) NOT NULL DEFAULT 0 COMMENT '最大学生数量',
  `enrolledCount` int(11) NOT NULL DEFAULT 0 COMMENT '已选人数',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `teacherId` bigint(20) NULL DEFAULT NULL COMMENT '授课老师',
  `classTimes` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上课时间(json存储)',
  `classRoom` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上课教室',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_classId`(`subjectId`) USING BTREE,
  INDEX `idx_courseSelectionId`(`courseSelectionId`) USING BTREE,
  INDEX `idx_course_selection_classedId_courseSelectionId`(`subjectId`, `courseSelectionId`) USING BTREE,
  INDEX `idx_course_selection_subject_classId_isDelete`(`subjectId`, `isDelete`) USING BTREE,
  INDEX `idx_course_selection_subject_courseSelectionId_isDelete`(`courseSelectionId`, `isDelete`) USING BTREE,
  INDEX `idx_courseSelectionId_isDelete_subjectId`(`courseSelectionId`, `isDelete`, `subjectId`, `enrolledCount`, `maxStudents`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1903168783209562115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '选课课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for department_info
-- ----------------------------
DROP TABLE IF EXISTS `department_info`;
CREATE TABLE `department_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院名称',
  `createUserId` bigint(20) NOT NULL COMMENT '添加该学院的用户id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_department_info_id_isDelete`(`id`, `isDelete`) USING BTREE,
  INDEX `idx_department_info_name_isDelete`(`name`, `isDelete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1788185471524823042 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学院信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for major_info
-- ----------------------------
DROP TABLE IF EXISTS `major_info`;
CREATE TABLE `major_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `departId` bigint(20) NOT NULL COMMENT '学院id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院名称',
  `createUserId` bigint(20) NOT NULL COMMENT '创建该目录用户id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_depart_id`(`departId`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_major_info_id_isDelete`(`id`, `isDelete`) USING BTREE,
  INDEX `idx_major_info_name_isDelete`(`name`, `isDelete`) USING BTREE,
  INDEX `idx_major_info_departId_isDelete`(`departId`, `isDelete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1788185757215645699 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for registration_task
-- ----------------------------
DROP TABLE IF EXISTS `registration_task`;
CREATE TABLE `registration_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登分任务id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `semesterId` bigint(20) NOT NULL COMMENT '学期id',
  `creatorId` bigint(20) NOT NULL COMMENT '添加人信息',
  `isActive` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否激活状态',
  `startDate` datetime NOT NULL COMMENT '开始日期',
  `endDate` datetime NOT NULL COMMENT '结束日期',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_registration_task_isDelete`(`isDelete`) USING BTREE,
  INDEX `registration_task_id_isActive_name_index`(`id`, `isActive`, `name`) USING BTREE COMMENT '激活索引',
  INDEX `registration_search_index`(`semesterId`, `isActive`, `id`, `isDelete`, `startDate`, `endDate`) USING BTREE COMMENT '查询登分任务要求'
) ENGINE = InnoDB AUTO_INCREMENT = 1883151614966198274 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登分任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for registration_task_lesson
-- ----------------------------
DROP TABLE IF EXISTS `registration_task_lesson`;
CREATE TABLE `registration_task_lesson`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登分任务课程字段id',
  `taskId` bigint(20) NOT NULL COMMENT '登分任务id',
  `lessonId` bigint(20) NOT NULL COMMENT '课程id',
  `courseTaskId` bigint(20) NOT NULL COMMENT '课程任务id',
  `isFinished` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否登分完成',
  `isPublish` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否发布成绩',
  `finishedTime` datetime NULL DEFAULT NULL COMMENT '完成日期',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `registration_task_lesson_id_isActive_name_index`(`id`, `courseTaskId`, `isFinished`, `isPublish`, `lessonId`) USING BTREE COMMENT '发布信息索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1883151614991364099 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登分任务课程信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for semesters
-- ----------------------------
DROP TABLE IF EXISTS `semesters`;
CREATE TABLE `semesters`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '学期id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学期名称，例如2023-2024学年第一学期',
  `isActive` int(11) NOT NULL DEFAULT 0 COMMENT '是否激活状态',
  `startDate` date NOT NULL COMMENT '学期开始日期',
  `endDate` date NOT NULL COMMENT '学期结束日期',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_semesters_name`(`name`) USING BTREE,
  INDEX `idx_semesters_isDelete`(`isDelete`) USING BTREE,
  INDEX `idx_semesters_isDelete_startDate`(`isDelete`, `startDate`) USING BTREE,
  INDEX `semesters_id_isActive_name_index`(`id`, `isActive`, `name`) USING BTREE COMMENT '激活索引',
  INDEX `idx_semesters_isActive_isDelete_startDate`(`isActive`, `isDelete`, `startDate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1872189933294247938 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学期管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for student_course_selection
-- ----------------------------
DROP TABLE IF EXISTS `student_course_selection`;
CREATE TABLE `student_course_selection`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '学生选课信息id',
  `studentId` bigint(20) NOT NULL COMMENT '学生ID',
  `courseSelectionId` bigint(20) NOT NULL COMMENT '选课信息ID，关联course_selection_info表',
  `subjectId` bigint(20) NOT NULL COMMENT '学生选课科目id',
  `selectTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `byRandom` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否是系统自动随机(0-否, 1-是)',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student_course_studentId`(`studentId`) USING BTREE,
  INDEX `idx_student_course_courseSelectionId`(`courseSelectionId`) USING BTREE,
  INDEX `idx_student_course_student_course`(`studentId`, `courseSelectionId`) USING BTREE,
  INDEX `idx_student_course_isDelete`(`isDelete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1887915812582412291 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生选课记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for student_grades
-- ----------------------------
DROP TABLE IF EXISTS `student_grades`;
CREATE TABLE `student_grades`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `stuId` bigint(20) NOT NULL COMMENT '学生ID，引用自student_score表',
  `subjectId` bigint(20) NOT NULL COMMENT '科目id',
  `semesterId` bigint(20) NOT NULL COMMENT '学期id',
  `totalGrade` int(11) NOT NULL COMMENT '成绩',
  `usualGrade` int(11) NULL DEFAULT NULL COMMENT '平时成绩',
  `finalGrade` int(11) NULL DEFAULT NULL COMMENT '期末成绩',
  `usualPercentage` tinyint(3) UNSIGNED NOT NULL COMMENT '平时分比例',
  `finalPercentage` tinyint(3) UNSIGNED NOT NULL COMMENT '期末分比例',
  `courseGroupId` bigint(20) NOT NULL COMMENT '课程组id',
  `creatorId` bigint(20) NOT NULL COMMENT '创建人id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '是否删除逻辑',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_grades_stuId_index`(`stuId`) USING BTREE,
  INDEX `student_grades_subjectId_index`(`subjectId`) USING BTREE,
  INDEX `student_grades_stuId_subjectId_index`(`stuId`, `subjectId`) USING BTREE COMMENT '学生查询成绩索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1883220430908641284 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生成绩表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
  `id` bigint(20) NOT NULL COMMENT '学生ID',
  `stuId` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学号',
  `stuName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生姓名',
  `stuSex` tinyint(11) NULL DEFAULT NULL COMMENT '学生性别',
  `stuDeptId` bigint(20) NOT NULL COMMENT '学生学院id',
  `stuMajorId` bigint(20) NOT NULL COMMENT '学生专业id',
  `stuClassId` bigint(20) NOT NULL COMMENT '学生班级Id',
  `creatorId` bigint(20) NOT NULL COMMENT '创建人Id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '是否删除逻辑键',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_info_pk`(`stuId`) USING BTREE,
  INDEX `student_info_stuName_index`(`stuName`) USING BTREE,
  INDEX `student_info_stuDeptId_stuMajorId_stuClassId_isDelete_index`(`stuDeptId`, `stuMajorId`, `stuClassId`, `isDelete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for subjects
-- ----------------------------
DROP TABLE IF EXISTS `subjects`;
CREATE TABLE `subjects`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '科目名称',
  `courseType` tinyint(4) NOT NULL DEFAULT 0 COMMENT '课程类型：必修-0/选修-1/',
  `creditHours` int(11) NOT NULL DEFAULT 1 COMMENT '课程学时 ',
  `gradeMax` int(11) NOT NULL DEFAULT 100 COMMENT '最高分',
  `gradeMin` int(11) NOT NULL DEFAULT 0 COMMENT '最低分',
  `gradeFail` int(11) NOT NULL DEFAULT 0 COMMENT '不及格分数线',
  `gradeExcellent` int(11) NOT NULL DEFAULT 60 COMMENT '优秀分数线',
  `gradeCredit` double NULL DEFAULT NULL COMMENT '课程学分',
  `creatorId` bigint(20) NOT NULL COMMENT '创建人id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `subjects_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1896468062074421252 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试科目信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for teacher_info
-- ----------------------------
DROP TABLE IF EXISTS `teacher_info`;
CREATE TABLE `teacher_info`  (
  `id` bigint(20) NOT NULL COMMENT '教师ID',
  `teacherId` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师工号',
  `teacherName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师姓名',
  `teacherSex` tinyint(11) NULL DEFAULT NULL COMMENT '教师性别',
  `teacherDeptId` bigint(20) NOT NULL COMMENT '教师学院id',
  `teacherMajorId` bigint(20) NOT NULL COMMENT '教师专业id',
  `creatorId` bigint(20) NOT NULL COMMENT '创建人Id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(11) NOT NULL DEFAULT 0 COMMENT '是否删除逻辑键',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `teacher_info_pk`(`teacherId`) USING BTREE,
  UNIQUE INDEX `teacher_info_pk_2`(`teacherId`) USING BTREE,
  INDEX `teacher_info_stuDeptId_stuMajorId_stuClassId_isDelete_index`(`teacherDeptId`, `teacherMajorId`, `isDelete`) USING BTREE,
  INDEX `teacher_info_stuName_index`(`teacherName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师信息表' ROW_FORMAT = DYNAMIC;

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
  `userClass` bigint(20) NULL DEFAULT NULL COMMENT '用户班级',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userTags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户标签',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `userRoleLevel` int(11) NOT NULL DEFAULT 0 COMMENT '角色权限等级(细分角色权限)',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_account`(`userAccount`) USING BTREE,
  INDEX `idx_user_userMajor`(`userDepartment`, `userMajor`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1896482400499789828 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
