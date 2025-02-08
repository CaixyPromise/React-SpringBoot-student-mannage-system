package com.caixy.adminSystem.model.vo.studentCourseSelection;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 选课学生信息
 *
 * @Author CAIXYPROMISE
 * @since 2025/2/6 2:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseStudentInfoVO extends StudentInfoVO
{
    private Date selectTime;


    /**
     * 是否是系统自动随机(0-否, 1-是)
     */
    private Integer byRandom;
}
