package com.caixy.adminSystem.model.dto.teacherInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建教师信息请求
 *


 */
@Data
public class TeacherInfoAddRequest implements Serializable {
    /**
     * 教师工号
     */
    private String teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教师性别
     */
    private Integer teacherSex;

    /**
     * 教师学院id
     */
    private Long teacherDeptId;

    /**
     * 教师专业id
     */
    private Long teacherMajorId;

    private static final long serialVersionUID = 1L;
}