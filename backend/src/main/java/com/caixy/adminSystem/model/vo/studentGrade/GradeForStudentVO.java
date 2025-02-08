package com.caixy.adminSystem.model.vo.studentGrade;

import lombok.Data;

import java.io.Serializable;

/**
 * 成绩信息VO
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/27 1:06
 */
@Data
public class GradeForStudentVO implements Serializable
{
    /**
     * 成绩id
     */
    private Long gradeId;
    /**
     * 科目id
     */
    private Long subjectId;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 科目成绩
     */
    private Integer totalGrade;

    /**
    * 不及格分数线
    */
    private Integer gradeFail;
    private static final long serialVersionUID = 1L;
}
