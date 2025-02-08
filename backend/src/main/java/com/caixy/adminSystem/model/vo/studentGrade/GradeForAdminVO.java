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

    /**
     * 科目满分
     */
    private Integer gradeMax;

    /**
     * 科目最低限度分
     */
    private Integer gradeMin;

    /**
     * 不及格分数线
     */
    private Integer gradeFail;

    /**
     * 优秀分数线
     */
    private Integer gradeExcellent;

    private static final long serialVersionUID = 1L;
}
