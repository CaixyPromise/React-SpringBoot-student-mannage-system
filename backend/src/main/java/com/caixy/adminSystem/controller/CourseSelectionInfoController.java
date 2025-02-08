package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.courseSelectionInfo.*;
import com.caixy.adminSystem.model.entity.CourseSelectionInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.Subjects.CourseSelectSubjectVO;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import com.caixy.adminSystem.service.CourseSelectionInfoService;
import com.caixy.adminSystem.service.CourseSelectionSubjectService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 选课信息表接口
 */
@RestController
@RequestMapping("/selection")
@Slf4j
public class CourseSelectionInfoController
{

    @Resource
    private CourseSelectionInfoService courseSelectionInfoService;

    @Resource
    private CourseSelectionSubjectService courseSelectionSubjectService;
    
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建选课信息表
     *
     * @param createCourseSelectionRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addCourseSelectionInfo(
            @RequestBody CreateCourseSelectionRequest createCourseSelectionRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(createCourseSelectionRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        Long selectionTaskId = courseSelectionInfoService.createCourseSelection(createCourseSelectionRequest,
                loginUser.getId());
        if (selectionTaskId == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(selectionTaskId);
    }

    /**
     * 删除选课信息表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCourseSelectionInfo(@RequestBody DeleteRequest deleteRequest,
                                                           HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        CourseSelectionInfo oldCourseSelectionInfo = courseSelectionInfoService.getById(id);
        ThrowUtils.throwIf(oldCourseSelectionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldCourseSelectionInfo, user);
        // 操作数据库
        boolean result = courseSelectionInfoService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新选课信息表（仅管理员可用）
     *
     * @param courseSelectionInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCourseSelectionInfo(
            @RequestBody CourseSelectionInfoUpdateRequest courseSelectionInfoUpdateRequest)
    {
        if (courseSelectionInfoUpdateRequest == null || courseSelectionInfoUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        CourseSelectionInfo courseSelectionInfo = new CourseSelectionInfo();
        BeanUtils.copyProperties(courseSelectionInfoUpdateRequest, courseSelectionInfo);
        // 数据校验
        courseSelectionInfoService.validCourseSelectionInfo(courseSelectionInfo, false);
        // 判断是否存在
        long id = courseSelectionInfoUpdateRequest.getId();
        CourseSelectionInfo oldCourseSelectionInfo = courseSelectionInfoService.getById(id);
        ThrowUtils.throwIf(oldCourseSelectionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = courseSelectionInfoService.updateById(courseSelectionInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取选课信息表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CourseSelectionInfoVO> getCourseSelectionInfoVOById(long id, HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoService.getById(id);
        ThrowUtils.throwIf(courseSelectionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(courseSelectionInfoService.getCourseSelectionInfoVO(courseSelectionInfo, request));
    }

    /**
     * 分页获取选课信息表列表（仅管理员可用）
     *
     * @param courseSelectionInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<CourseSelectionInfo>> listCourseSelectionInfoByPage(
            @RequestBody CourseSelectionInfoQueryRequest courseSelectionInfoQueryRequest)
    {
        long current = courseSelectionInfoQueryRequest.getCurrent();
        long size = courseSelectionInfoQueryRequest.getPageSize();
        // 查询数据库
        Page<CourseSelectionInfo> courseSelectionInfoPage = courseSelectionInfoService.page(new Page<>(current, size),
                courseSelectionInfoService.getQueryWrapper(courseSelectionInfoQueryRequest));
        return ResultUtils.success(courseSelectionInfoPage);
    }

    /**
     * 分页获取选课信息表列表（封装类）
     *
     * @param courseSelectionInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CourseSelectionInfoVO>> listCourseSelectionInfoVOByPage(
            @RequestBody CourseSelectionInfoQueryRequest courseSelectionInfoQueryRequest,
            HttpServletRequest request)
    {
        long current = courseSelectionInfoQueryRequest.getCurrent();
        long size = courseSelectionInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CourseSelectionInfo> courseSelectionInfoPage = courseSelectionInfoService.page(new Page<>(current, size),
                courseSelectionInfoService.getQueryWrapper(courseSelectionInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(
                courseSelectionInfoService.getCourseSelectionInfoVOPage(courseSelectionInfoPage, request));
    }

    /**
     * 分页获取当前登录用户创建的选课信息表列表
     *
     * @param courseSelectionInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CourseSelectionInfoVO>> listMyCourseSelectionInfoVOByPage(
            @RequestBody CourseSelectionInfoQueryRequest courseSelectionInfoQueryRequest,
            HttpServletRequest request)
    {
        ThrowUtils.throwIf(courseSelectionInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        courseSelectionInfoQueryRequest.setUserId(loginUser.getId());
        long current = courseSelectionInfoQueryRequest.getCurrent();
        long size = courseSelectionInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<CourseSelectionInfo> courseSelectionInfoPage = courseSelectionInfoService.page(new Page<>(current, size),
                courseSelectionInfoService.getQueryWrapper(courseSelectionInfoQueryRequest));
        // 获取封装类
        return ResultUtils.success(
                courseSelectionInfoService.getCourseSelectionInfoVOPage(courseSelectionInfoPage, request));
    }

    /**
     * 编辑选课信息表（给用户使用）
     *
     * @param courseSelectionInfoEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editCourseSelectionInfo(
            @RequestBody CourseSelectionInfoEditRequest courseSelectionInfoEditRequest, HttpServletRequest request)
    {
        if (courseSelectionInfoEditRequest == null || courseSelectionInfoEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        CourseSelectionInfo courseSelectionInfo = new CourseSelectionInfo();
        BeanUtils.copyProperties(courseSelectionInfoEditRequest, courseSelectionInfo);
        // 数据校验
        courseSelectionInfoService.validCourseSelectionInfo(courseSelectionInfo, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = courseSelectionInfoEditRequest.getId();
        CourseSelectionInfo oldCourseSelectionInfo = courseSelectionInfoService.getById(id);
        ThrowUtils.throwIf(oldCourseSelectionInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldCourseSelectionInfo, loginUser);
        // 操作数据库
        boolean result = courseSelectionInfoService.updateById(courseSelectionInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页查询选课任务列表
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/3 14:28
     */
    @GetMapping("/page/selection-task-admin")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<CourseSelectionInfoVO>> pageCourseSelection(
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) String taskName
    )
    {
        Page<CourseSelectionInfoVO> result = courseSelectionInfoService
                .pageCourseSelection(pageNum, pageSize, semesterId, taskName);
        return ResultUtils.success(result);
    }

    /**
     * 获取选课任务的选课科目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/10 3:18
     */
    @GetMapping("/get/select-task/courses")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<CourseSelectSubjectVO>> getSelectTaskCoursesByTaskId(@RequestParam Long taskId)
    {
        if (taskId < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseSelectSubjectVO> result = courseSelectionInfoService.getSelectTaskCourses(taskId);
        return ResultUtils.success(result);
    }

    /**
     * 搁置任务
     */
    @GetMapping("/put-task-hold")
    public BaseResponse<Boolean> putTaskHoldById(@RequestParam Long taskId)
    {
        if (taskId < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseSelectionInfoService.putTaskHoldById(taskId);
        return ResultUtils.success(result);
    }

    /**
     * 激活任务
     */
    @GetMapping("/activate-task")
    public BaseResponse<Boolean> activateTaskById(@RequestParam Long taskId)
    {
        if (taskId < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseSelectionInfoService.putTaskActiveById(taskId);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/student/available-selection-task")
    @AuthCheck(mustRole = UserConstant.STUDENT_ROLE)
    public BaseResponse<List<CourseSelectionInfoVO>> getUserCourseSelectionInfo(HttpServletRequest request)
    {
        User loginUser = userService.getLoginUser(request);
        List<CourseSelectionInfoVO> courseSelectionInfoVO = courseSelectionInfoService
                .getStudentTasks(loginUser.getId());
        return ResultUtils.success(courseSelectionInfoVO);
    }

    @GetMapping("/get/bySemesterId")
    public BaseResponse<List<CourseSelectionInfoVO>> getCourseSelectionInfoBySemesterId(@RequestParam Long semesterId)
    {
        List<CourseSelectionInfoVO> courseSelectionInfoVO = courseSelectionInfoService.getCourseSelectionInfoBySemesterId(
                semesterId);
        return ResultUtils.success(courseSelectionInfoVO);
    }

    /**
     * 获取任务下的科目id
     * 
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/21 3:11
     */
    @GetMapping("/get/subject/by-taskId")
    public BaseResponse<List<SubjectsVO>> getCourseSelectSubjectByTaskId(
            @RequestParam Long taskId
    ) {
        List<SubjectsVO> courseSelectSubjectVO = courseSelectionSubjectService.getSubjectVOByTaskId(taskId);
        return ResultUtils.success(courseSelectSubjectVO);
    }


    // endregion

    private void checkIsSelfOrAdmin(CourseSelectionInfo courseSelectionInfo, User loginUser)
    {
//        if (!courseSelectionInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
//        {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
    }
}
