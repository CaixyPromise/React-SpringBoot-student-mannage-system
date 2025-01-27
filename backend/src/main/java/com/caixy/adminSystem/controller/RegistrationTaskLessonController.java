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
import com.caixy.adminSystem.model.dto.registrationTaskLesson.*;
import com.caixy.adminSystem.model.entity.RegistrationTaskLesson;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.HasRegistrationTaskVO;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.RegistrationTaskLessonVO;
import com.caixy.adminSystem.service.RegistrationTaskLessonService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 登分任务课程信息接口
 */
@RestController
@RequestMapping("/registrationTaskLesson")
@Slf4j
public class RegistrationTaskLessonController
{

    @Resource
    private RegistrationTaskLessonService registrationTaskLessonService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建登分任务课程信息
     *
     * @param registrationTaskLessonAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRegistrationTaskLesson(@RequestBody RegistrationTaskLessonAddRequest registrationTaskLessonAddRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(registrationTaskLessonAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        RegistrationTaskLesson registrationTaskLesson = new RegistrationTaskLesson();
        BeanUtils.copyProperties(registrationTaskLessonAddRequest, registrationTaskLesson);
        // 数据校验
        registrationTaskLessonService.validRegistrationTaskLesson(registrationTaskLesson, true);
        // todo 填充默认值
        // 写入数据库
        boolean result = registrationTaskLessonService.save(registrationTaskLesson);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newRegistrationTaskLessonId = registrationTaskLesson.getId();
        return ResultUtils.success(newRegistrationTaskLessonId);
    }

    /**
     * 删除登分任务课程信息
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRegistrationTaskLesson(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        RegistrationTaskLesson oldRegistrationTaskLesson = registrationTaskLessonService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTaskLesson == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldRegistrationTaskLesson, user);
        // 操作数据库
        boolean result = registrationTaskLessonService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新登分任务课程信息（仅管理员可用）
     *
     * @param registrationTaskLessonUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRegistrationTaskLesson(@RequestBody RegistrationTaskLessonUpdateRequest registrationTaskLessonUpdateRequest)
    {
        if (registrationTaskLessonUpdateRequest == null || registrationTaskLessonUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        RegistrationTaskLesson registrationTaskLesson = new RegistrationTaskLesson();
        BeanUtils.copyProperties(registrationTaskLessonUpdateRequest, registrationTaskLesson);
        // 数据校验
        registrationTaskLessonService.validRegistrationTaskLesson(registrationTaskLesson, false);
        // 判断是否存在
        long id = registrationTaskLessonUpdateRequest.getId();
        RegistrationTaskLesson oldRegistrationTaskLesson = registrationTaskLessonService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTaskLesson == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = registrationTaskLessonService.updateById(registrationTaskLesson);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取登分任务课程信息列表（仅管理员可用）
     *
     * @param registrationTaskLessonQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<RegistrationTaskLesson>> listRegistrationTaskLessonByPage(@RequestBody RegistrationTaskLessonQueryRequest registrationTaskLessonQueryRequest)
    {
        long current = registrationTaskLessonQueryRequest.getCurrent();
        long size = registrationTaskLessonQueryRequest.getPageSize();
        // 查询数据库
        Page<RegistrationTaskLesson> registrationTaskLessonPage = registrationTaskLessonService.page(new Page<>(current, size), registrationTaskLessonService.getQueryWrapper(registrationTaskLessonQueryRequest));
        return ResultUtils.success(registrationTaskLessonPage);
    }

    /**
     * 分页获取登分任务课程信息列表（封装类）
     *
     * @param registrationTaskLessonQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RegistrationTaskLessonVO>> listRegistrationTaskLessonVOByPage(@RequestBody RegistrationTaskLessonQueryRequest registrationTaskLessonQueryRequest, HttpServletRequest request)
    {
        long current = registrationTaskLessonQueryRequest.getCurrent();
        long size = registrationTaskLessonQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<RegistrationTaskLesson> registrationTaskLessonPage = registrationTaskLessonService.page(new Page<>(current, size), registrationTaskLessonService.getQueryWrapper(registrationTaskLessonQueryRequest));
        // 获取封装类
        return ResultUtils.success(registrationTaskLessonService.getRegistrationTaskLessonVOPage(registrationTaskLessonPage, request));
    }

    /**
     * 分页获取当前登录用户创建的登分任务课程信息列表
     *
     * @param registrationTaskLessonQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<RegistrationTaskLessonVO>> listMyRegistrationTaskLessonVOByPage(@RequestBody RegistrationTaskLessonQueryRequest registrationTaskLessonQueryRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(registrationTaskLessonQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        registrationTaskLessonQueryRequest.setUserId(loginUser.getId());
        long current = registrationTaskLessonQueryRequest.getCurrent();
        long size = registrationTaskLessonQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<RegistrationTaskLesson> registrationTaskLessonPage = registrationTaskLessonService.page(new Page<>(current, size), registrationTaskLessonService.getQueryWrapper(registrationTaskLessonQueryRequest));
        // 获取封装类
        return ResultUtils.success(registrationTaskLessonService.getRegistrationTaskLessonVOPage(registrationTaskLessonPage, request));
    }

    /**
     * 编辑登分任务课程信息（给用户使用）
     *
     * @param registrationTaskLessonEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editRegistrationTaskLesson(@RequestBody RegistrationTaskLessonEditRequest registrationTaskLessonEditRequest, HttpServletRequest request)
    {
        if (registrationTaskLessonEditRequest == null || registrationTaskLessonEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        RegistrationTaskLesson registrationTaskLesson = new RegistrationTaskLesson();
        BeanUtils.copyProperties(registrationTaskLessonEditRequest, registrationTaskLesson);
        // 数据校验
        registrationTaskLessonService.validRegistrationTaskLesson(registrationTaskLesson, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = registrationTaskLessonEditRequest.getId();
        RegistrationTaskLesson oldRegistrationTaskLesson = registrationTaskLessonService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTaskLesson == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldRegistrationTaskLesson, loginUser);
        // 操作数据库
        boolean result = registrationTaskLessonService.updateById(registrationTaskLesson);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/get/hasTasks")
    public BaseResponse<List<HasRegistrationTaskVO>> getHasTasksBySubjectIdAndCourseTaskIds(@RequestBody @Valid HasRegistrationTaskRequest hasRegistrationTaskRequest)
    {
        return ResultUtils.success(registrationTaskLessonService.getHasTasksBySubjectIdAndCourseTaskIds(hasRegistrationTaskRequest.getSubjectId(), hasRegistrationTaskRequest.getCourseTaskIds()));
    }

    /**
     * 获取等分任务课程信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/27 1:25
     */
    @GetMapping("/get/registrationTaskLessonVO/{id}")
    public BaseResponse<List<RegistrationTaskLessonVO>> listRegistrationTaskLessonVOByPage(@PathVariable("id") Long id)
    {
        return ResultUtils.success(registrationTaskLessonService.getRegistrationLessonByTaskId(id));
    }

    @PostMapping("/update/publication")
    public BaseResponse<Boolean> updatePublication(@RequestBody @Valid RegistrationTaskLessonPublicationRequest registrationTaskLessonPublicationRequest)
    {
        return ResultUtils.success(registrationTaskLessonService.publicationGradeInfo(registrationTaskLessonPublicationRequest));
    }

    // endregion

    private void checkIsSelfOrAdmin(RegistrationTaskLesson registrationTaskLesson, User loginUser)
    {

    }
}
