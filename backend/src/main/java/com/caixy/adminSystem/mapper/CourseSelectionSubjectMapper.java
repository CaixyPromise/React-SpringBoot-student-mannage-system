package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.entity.CourseSelectionSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【course_selection_subject】的数据库操作Mapper
* @createDate 2025-01-10 01:42:59
* @Entity com.caixy.adminSystem.model.entity.CourseSelectionSubject
*/
public interface CourseSelectionSubjectMapper extends BaseMapper<CourseSelectionSubject> {
    /**
     * 根据选课任务 ID 查询关联的课程科目信息
     * @param courseSelectionId 选课任务 ID
     * @return 科目列表
     */
    List<SubjectsVO> getSubjectsByCourseSelectionId(@Param("courseSelectionId") Long courseSelectionId);


    /**
     * 根据选课任务ID查询所有可选科目信息
     * 子查询或多表方式都可以，这里我们示例使用子查询
     * 只查 course_selection_subject，不用 join
     */
    List<Long> getSubjectIdsBySelectionId(
            @Param("courseSelectionId") Long courseSelectionId
    );

    /**
     * 根据 选课任务ID 查询所有可选科目的基础信息（包含已选人数、最大人数等）
     */
    List<CourseSelectionSubject> getSubjectListBySelectionId(
            @Param("courseSelectionId") Long courseSelectionId
    );

    /**
     * 根据 选课任务ID 和 科目IDs，查询对应记录
     * 确保这些科目确实属于该选课任务
     */
    List<CourseSelectionSubject> listBySelectionAndSubjects(
            @Param("courseSelectionId") Long courseSelectionId,
            @Param("subjectIds") List<Long> subjectIds
    );

    /**
     * 批量更新已选人数（+1）
     */
    int incrementEnrolledCountBatch(
            @Param("ids") List<Long> ids
    );

    int decrementEnrolledCountBatch(@Param("ids") List<Long> cssIdList);

    List<CourseSelectionSubject> findCourseSelectionSubjectByCourseSelectionId(Long courseSelectionId);

    int batchUpdateEnrolledCount(List<CourseSelectionSubject> updates);
}




