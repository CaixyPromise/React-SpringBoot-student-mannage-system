package com.caixy.adminSystem.model.vo.teacherInfo;

import com.caixy.adminSystem.model.dto.subject.SubjectClassTime;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分配教师选修课程信息
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/17 23:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssignedTeacherLessonInfo extends SubjectsVO
{
    /**
     * 上课时间
     */
    private List<SubjectClassTime> classTimes;

    /**
     * 上课教室
     */
    private String classRoom;
}
