package com.caixy.adminSystem.model.vo.studentGrade;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学生成绩vo
 *
 * @name: com.caixy.adminSystem.model.vo.student.StudentVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-02 22:58
 **/
@Data
public class StudentsGradeForAdminVO implements Serializable
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
    private GradeForAdminVO gradeItem;


    private static final long serialVersionUID = 1L;
}
