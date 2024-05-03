package com.caixy.adminSystem.model.vo.StudentGrades;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 成绩信息
     */
    private List<GradeItem> gradeItem;

    /**
     * 成绩信息
     */
    @Data
    public static class GradeItem implements Serializable
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
        private Long grade;
        private static final long serialVersionUID = 1L;
    }

    private static final long serialVersionUID = 1L;
}
