package com.caixy.adminSystem.model.vo.studentCourseSelection;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 不满足选课学分条件的学生信息VO
 *
 * @Author CAIXYPROMISE
 * @since 2025/2/6 21:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentWithCourseSelectionVO extends StudentInfoVO
{
    /**
    * 当前已选学分
    */
    private Double totalCredit;
    /**
    * 需要的学分
    */
    private Double requiredCredit;
    /**
    * 已选课程
    */
    private List<SubjectsVO> selectedSubjects;
}
