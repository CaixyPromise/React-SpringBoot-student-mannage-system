package com.caixy.adminSystem.model.vo.StudentInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生信息VO
 *
 * @name: com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-28 21:50
 **/
@Data
public class StudentInfoVO implements Serializable
{
    /**
     * 学生id
     */
    private Long id;

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 学生性别
     */
    private Integer stuSex;

    /**
     * 学生学院
     */
    private String stuDepart;

    /**
     * 学生专业
     */
    private String stuMajor;

    /**
     * 学生班级
     */
    private String stuClass;

    /**
     * 学生学院id
     */
    private Long stuDeptId;

    /**
     * 学生专业id
     */
    private Long stuMajorId;

    /**
     * 学生班级id
     */
    private Long stuClassId;

    private static final long serialVersionUID = 1L;
}
