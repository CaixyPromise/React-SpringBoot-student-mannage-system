package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.registrationTaskLesson.RegistrationTaskLessonPublicationRequest;
import com.caixy.adminSystem.model.dto.registrationTaskLesson.RegistrationTaskLessonQueryRequest;
import com.caixy.adminSystem.model.entity.RegistrationTaskLesson;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.HasRegistrationTaskVO;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.RegistrationTaskLessonVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登分任务课程信息服务
 *


 */
public interface RegistrationTaskLessonService extends IService<RegistrationTaskLesson> {

    /**
     * 校验数据
     *
     * @param registrationTaskLesson
     * @param add 对创建的数据进行校验
     */
    void validRegistrationTaskLesson(RegistrationTaskLesson registrationTaskLesson, boolean add);

    /**
     * 获取查询条件
     *
     * @param registrationTaskLessonQueryRequest
     * @return
     */
    QueryWrapper<RegistrationTaskLesson> getQueryWrapper(RegistrationTaskLessonQueryRequest registrationTaskLessonQueryRequest);
    
    /**
     * 获取登分任务课程信息封装
     *
     * @param registrationTaskLesson
     * @param request
     * @return
     */
    List<RegistrationTaskLessonVO> getRegistrationLessonByTaskId(Long taskId);

    /**
     * 分页获取登分任务课程信息封装
     *
     * @param registrationTaskLessonPage
     * @param request
     * @return
     */
    Page<RegistrationTaskLessonVO> getRegistrationTaskLessonVOPage(Page<RegistrationTaskLesson> registrationTaskLessonPage, HttpServletRequest request);

    List<HasRegistrationTaskVO> getHasTasksBySubjectIdAndCourseTaskIds(Long subjectId, List<Long> courseTaskIds);

    Boolean publicationGradeInfo(RegistrationTaskLessonPublicationRequest request);
}
