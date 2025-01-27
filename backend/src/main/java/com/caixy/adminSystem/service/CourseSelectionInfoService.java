package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.courseSelectionInfo.CourseSelectionInfoQueryRequest;
import com.caixy.adminSystem.model.dto.courseSelectionInfo.CreateCourseSelectionRequest;
import com.caixy.adminSystem.model.entity.CourseSelectionInfo;
import com.caixy.adminSystem.model.vo.Subjects.CourseSelectSubjectVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 选课信息表服务
 *


 */
public interface CourseSelectionInfoService extends IService<CourseSelectionInfo> {

    /**
     * 校验数据
     *
     * @param courseSelectionInfo
     * @param add 对创建的数据进行校验
     */
    void validCourseSelectionInfo(CourseSelectionInfo courseSelectionInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param courseSelectionInfoQueryRequest
     * @return
     */
    QueryWrapper<CourseSelectionInfo> getQueryWrapper(CourseSelectionInfoQueryRequest courseSelectionInfoQueryRequest);

    /**
     * 获取选课信息表封装
     *
     * @param courseSelectionInfo
     * @param request
     * @return
     */
    CourseSelectionInfoVO getCourseSelectionInfoVO(CourseSelectionInfo courseSelectionInfo, HttpServletRequest request);

    /**
     * 分页获取选课信息表封装
     *
     * @param courseSelectionInfoPage
     * @param request
     * @return
     */
    Page<CourseSelectionInfoVO> getCourseSelectionInfoVOPage(Page<CourseSelectionInfo> courseSelectionInfoPage, HttpServletRequest request);

    Long createCourseSelection(CreateCourseSelectionRequest request, Long currentUserId);

    Page<CourseSelectionInfoVO> pageCourseSelection(
            int pageNum,
            int pageSize,
            Long semesterId, String taskName);

    Boolean putTaskHoldById(Long taskId);

    Boolean putTaskActiveById(Long taskId);

    List<CourseSelectionInfoVO> getCourseTaskByIds(Collection<Long> courseSelectionIds);

    List<CourseSelectSubjectVO> getSelectTaskCourses(Long taskId);

    List<CourseSelectionInfoVO> getStudentTasks(Long studentId);

    List<CourseSelectionInfoVO> getCourseSelectionInfoBySemesterId(Long semesterId);
}
