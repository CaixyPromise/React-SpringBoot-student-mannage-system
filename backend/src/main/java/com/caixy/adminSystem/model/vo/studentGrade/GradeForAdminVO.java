package com.caixy.adminSystem.model.vo.studentGrade;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 成绩信息VO
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/27 1:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GradeForAdminVO extends GradeForStudentVO implements Serializable
{
    /**
     * 平时成绩
     */
    private Integer usualGrade;

    /**
     * 期末成绩
     */
    private Integer finalGrade;

    /**
     * 平时分比例
     */
    private Integer usualPercentage;

    /**
     * 期末分比例
     */
    private Integer finalPercentage;

    private static final long serialVersionUID = 1L;
}
