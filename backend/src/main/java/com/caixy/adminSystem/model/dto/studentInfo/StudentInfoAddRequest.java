package com.caixy.adminSystem.model.dto.studentInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 
 */
@Data
public class StudentInfoAddRequest implements Serializable {

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 学生性别
     */
    private Integer stuSex;

    /**
     * 学生学院id
     */
    private Long stuDeptId;

    /**
     * 学生专业id
     */
    private Long stuMajorId;

    /**
     * 学生班级Id
     */
    private Long stuClassId;

    private static final long serialVersionUID = 1L;
}