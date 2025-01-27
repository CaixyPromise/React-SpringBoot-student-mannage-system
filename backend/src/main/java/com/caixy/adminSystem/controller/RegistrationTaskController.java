package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskAddRequest;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskEditRequest;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskQueryRequest;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskUpdateRequest;
import com.caixy.adminSystem.model.entity.RegistrationTask;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.enums.RegistrationActiveEnum;
import com.caixy.adminSystem.model.vo.registrationTask.RegistrationTaskVO;
import com.caixy.adminSystem.service.RegistrationTaskService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 登分任务接口
 */
@RestController
@RequestMapping("/registrationTask")
@Slf4j
public class RegistrationTaskController
{

    @Resource
    private RegistrationTaskService registrationTaskService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建登分任务
     *
     * @param registrationTaskAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addRegistrationTask(@RequestBody @Valid RegistrationTaskAddRequest registrationTaskAddRequest, HttpServletRequest request)
    {
        ThrowUtils.throwIf(registrationTaskAddRequest == null, ErrorCode.PARAMS_ERROR);
        Boolean result = registrationTaskService.addRegistrationTask(registrationTaskAddRequest, userService.getLoginUser(request).getId());
        return ResultUtils.success(result);
    }

    /**
     * 删除登分任务
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRegistrationTask(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        RegistrationTask oldRegistrationTask = registrationTaskService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTask == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldRegistrationTask, user);
        // 操作数据库
        boolean result = registrationTaskService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新登分任务（仅管理员可用）
     *
     * @param registrationTaskUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRegistrationTask(@RequestBody RegistrationTaskUpdateRequest registrationTaskUpdateRequest)
    {
        if (registrationTaskUpdateRequest == null || registrationTaskUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        RegistrationTask registrationTask = new RegistrationTask();
        BeanUtils.copyProperties(registrationTaskUpdateRequest, registrationTask);
        // 数据校验
        registrationTaskService.validRegistrationTask(registrationTask, false);
        // 判断是否存在
        long id = registrationTaskUpdateRequest.getId();
        RegistrationTask oldRegistrationTask = registrationTaskService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTask == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = registrationTaskService.updateById(registrationTask);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取登分任务（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<RegistrationTaskVO> getRegistrationTaskVOById(long id, HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        RegistrationTask registrationTask = registrationTaskService.getById(id);
        ThrowUtils.throwIf(registrationTask == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(registrationTaskService.getRegistrationTaskVO(registrationTask, request));
    }

    /**
     * 分页获取登分任务列表（仅管理员可用）
     *
     * @param registrationTaskQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<RegistrationTaskVO>> listRegistrationTaskByPage(@RequestBody RegistrationTaskQueryRequest registrationTaskQueryRequest)
    {
        long current = registrationTaskQueryRequest.getCurrent();
        long size = registrationTaskQueryRequest.getPageSize();
        if (current <= 0 || size <= 0 || size > 100)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询数据库
        return ResultUtils.success(registrationTaskService.getPage(registrationTaskQueryRequest));
    }

    /**
     * 编辑登分任务（给用户使用）
     *
     * @param registrationTaskEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editRegistrationTask(@RequestBody RegistrationTaskEditRequest registrationTaskEditRequest, HttpServletRequest request)
    {
        if (registrationTaskEditRequest == null || registrationTaskEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        RegistrationTask registrationTask = new RegistrationTask();
        BeanUtils.copyProperties(registrationTaskEditRequest, registrationTask);
        // 数据校验
        registrationTaskService.validRegistrationTask(registrationTask, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = registrationTaskEditRequest.getId();
        RegistrationTask oldRegistrationTask = registrationTaskService.getById(id);
        ThrowUtils.throwIf(oldRegistrationTask == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldRegistrationTask, loginUser);
        // 操作数据库
        boolean result = registrationTaskService.updateById(registrationTask);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/edit/active")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> activeRegistrationTaskUsingPost(@RequestBody @Valid RegistrationTaskEditRequest registrationTaskEditRequest, HttpServletRequest request)
    {
        if (registrationTaskEditRequest == null || registrationTaskEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer isActive = registrationTaskEditRequest.getIsActive();
        RegistrationActiveEnum activeEnum = RegistrationActiveEnum.getEnumByCode(isActive);
        ThrowUtils.throwIf(activeEnum == null, ErrorCode.PARAMS_ERROR);
        // 查找任务是否存在
        LambdaQueryWrapper<RegistrationTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationTask::getId, registrationTaskEditRequest.getId());
        RegistrationTask registrationTask = registrationTaskService.getOne(queryWrapper);
        ThrowUtils.throwIf(registrationTask == null, ErrorCode.NOT_FOUND_ERROR);
        // 如果状态一致，无需更新
        if (registrationTask.getIsActive().equals(isActive)) {
            return ResultUtils.success(true);
        }
        registrationTask.setIsActive(isActive);
        return ResultUtils.success(registrationTaskService.updateById(registrationTask));
    }

    // endregion

    private void checkIsSelfOrAdmin(RegistrationTask registrationTask, User loginUser)
    {

    }
}
