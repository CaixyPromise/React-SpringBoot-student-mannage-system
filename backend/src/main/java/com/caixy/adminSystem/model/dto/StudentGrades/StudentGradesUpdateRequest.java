package com.caixy.adminSystem.model.dto.StudentGrades;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class StudentGradesUpdateRequest implements Serializable
{
    /**
     * 成绩id
     */
    Long id;

    /**
     * 成绩
     */
    Long grade;
    private static final long serialVersionUID = 1L;
}