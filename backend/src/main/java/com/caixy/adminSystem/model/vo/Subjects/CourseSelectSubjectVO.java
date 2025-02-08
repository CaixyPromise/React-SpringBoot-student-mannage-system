package com.caixy.adminSystem.model.vo.Subjects;

import com.caixy.adminSystem.model.dto.courseSelectionInfo.CreateCourseSelectionRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectClassTime;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 选课任务科目vo
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/17 22:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSelectSubjectVO extends SubjectsVO
{

    /**
     * 教师信息
     */
    private TeacherInfoVO teacherInfo;

    /**
     * 上课时间
     */
    private List<SubjectClassTime> classTimes;

    /**
     * 上课地点
     */
    private String classRoom;

    /**
    * 已选人数
    */
    private Integer enrollCount;
}
