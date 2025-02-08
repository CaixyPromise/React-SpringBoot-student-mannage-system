package com.caixy.adminSystem.model.vo.Subjects;

import com.caixy.adminSystem.model.dto.courseSelectionInfo.CreateCourseSelectionRequest;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 科目VO返回类
 *
 * @name: com.caixy.adminSystem.model.vo.Subjects.SubjectsVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-29 21:57
 **/
@Data
public class SubjectsVO implements Serializable
{
    /**
     * 科目id
     */
    private Long id;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 课程类型
     */
    private Integer courseType;

    /**
     * 课程学分
     */
    private Double gradeCredit;

    /**
     * 科目最大值
     */
    private Integer gradeMax;

    /**
     * 科目最小值
     */
    private Integer gradeMin;

    /**
     * 优秀分数线
     */
    private Integer gradeExcellent;

    /**
     * 不及格分数线
     */
    private Integer gradeFail;

    /**
     * 课程学时
     */
    private Integer creditHours;

    /**
    * 最大学生人数
    */
    private Integer maxStudent;

    private static final long serialVersionUID = 1L;
}
