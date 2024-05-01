package com.caixy.adminSystem.model.vo.StudentGrades;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子视图
 */
@Data
public class StudentGradesVO implements Serializable
{
    /**
     * 学生ID，引用自student_score表
     */
    private Long stuId;

    /**
     * 学生信息
     */
    private StudentInfoVO studentInfo;

    @Data
    public static class GradeItem implements Serializable
    {
        private String subjectName;
        private Long grade;
        private static final long serialVersionUID = 1L;
    }

    private static final long serialVersionUID = 1L;
}
