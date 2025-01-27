package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.entity.RegistrationTaskLesson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.HasRegistrationTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【registration_task_lesson(登分任务课程信息表)】的数据库操作Mapper
* @createDate 2025-01-19 23:55:23
* @Entity com.caixy.adminSystem.model.entity.RegistrationTaskLesson
*/
public interface RegistrationTaskLessonMapper extends BaseMapper<RegistrationTaskLesson> {
    List<HasRegistrationTaskVO> selectHasRegistrationTaskBySubjectAndCourseTaskIds(@Param("subjectId") Long subjectId,
                                                                                   @Param("courseTaskIds") List<Long> courseTaskIds);
}




