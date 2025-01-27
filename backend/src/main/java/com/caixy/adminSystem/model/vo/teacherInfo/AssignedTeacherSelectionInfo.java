package com.caixy.adminSystem.model.vo.teacherInfo;

import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教师分配课程信息
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/17 23:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssignedTeacherSelectionInfo extends AssignedTeacherLessonInfo
{
    /**
     * 最大选课人数, 为0时不限人数
     */
    private Integer maxStudents;

    /**
     * 当前选课人数
     */
    private Integer enrolledCount;

    /**
     * 选课信息ID，关联course_selection_info表
     */
    private Long courseSelectionId;

    /**
    * 科目ID，关联subject表
    */
    private CourseSelectionInfoVO courseSelectionInfoVO;
}
