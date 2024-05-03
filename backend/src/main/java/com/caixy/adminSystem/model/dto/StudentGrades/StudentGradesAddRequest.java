package com.caixy.adminSystem.model.dto.StudentGrades;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class StudentGradesAddRequest implements Serializable
{

    /**
     * 科目id
     */
    private Long subjectId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 成绩
     */
    private Long score;


    private static final long serialVersionUID = 1L;
}