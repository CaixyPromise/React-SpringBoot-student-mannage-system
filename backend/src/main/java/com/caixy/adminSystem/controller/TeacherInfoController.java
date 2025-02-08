package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.*;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoAddRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoEditRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoQueryRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoUpdateRequest;
import com.caixy.adminSystem.model.entity.TeacherInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.enums.UserRoleEnum;
import com.caixy.adminSystem.model.vo.teacherInfo.AssignedTeacherLessonInfo;
import com.caixy.adminSystem.model.vo.teacherInfo.AssignedTeacherSelectionInfo;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import com.caixy.adminSystem.service.TeacherInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 教师信息接口控制器
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/11 19:12
 */
@Slf4j
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherInfoController
{
    private final TeacherInfoService teacherInfoService;
    private final UserService userService;

    // region 增删改查

    /**
     * 创建教师信息
     *
     * @param teacherInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeacherInfo(@RequestBody TeacherInfoAddRequest teacherInfoAddRequest,
                                             HttpServletRequest request)
    {
        ThrowUtils.throwIf(teacherInfoAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        TeacherInfo teacherInfo = new TeacherInfo();
        BeanUtils.copyProperties(teacherInfoAddRequest, teacherInfo);
        // 数据校验
        teacherInfoService.validTeacherInfo(teacherInfo, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        teacherInfo.setCreatorId(loginUser.getId());
        // 写入数据库
        boolean result = teacherInfoService.save(teacherInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newTeacherInfoId = teacherInfo.getId();
        User newUser = User.builder()
                           .userAccount(teacherInfo.getTeacherId())
                           .userPassword(teacherInfo.getTeacherId())
                           .id(newTeacherInfoId)
                           .userDepartment(teacherInfo.getTeacherDeptId())
                           .userMajor(teacherInfo.getTeacherMajorId())
                           .userRoleLevel(0)
                           .userRole(UserRoleEnum.TEACHER.getValue())
                           .userName(teacherInfo.getTeacherName())
                           .userSex(teacherInfo.getTeacherSex())
                           .build();
        userService.makeRegister(newUser);
        return ResultUtils.success(newTeacherInfoId);
    }

    /**
     * 删除教师信息
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeacherInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TeacherInfo oldTeacherInfo = teacherInfoService.getById(id);
        ThrowUtils.throwIf(oldTeacherInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldTeacherInfo, user);
        // 操作数据库
        boolean result = teacherInfoService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新教师信息（仅管理员可用）
     *
     * @param teacherInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTeacherInfo(@RequestBody TeacherInfoUpdateRequest teacherInfoUpdateRequest)
    {
        if (teacherInfoUpdateRequest == null || teacherInfoUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        TeacherInfo teacherInfo = new TeacherInfo();
        BeanUtils.copyProperties(teacherInfoUpdateRequest, teacherInfo);
        // 数据校验
        teacherInfoService.validTeacherInfo(teacherInfo, false);
        // 判断是否存在
        long id = teacherInfoUpdateRequest.getId();
        TeacherInfo oldTeacherInfo = teacherInfoService.getById(id);
        ThrowUtils.throwIf(oldTeacherInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = teacherInfoService.updateById(teacherInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取教师信息（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TeacherInfoVO> getTeacherInfoVOById(long id, HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        TeacherInfo teacherInfo = teacherInfoService.getById(id);
        ThrowUtils.throwIf(teacherInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(teacherInfoService.getTeacherInfoVO(teacherInfo, request));
    }

    @PostMapping("/get/option")
    public BaseResponse<IPage<TeacherInfoVO>> getTeacherInfoOptionVO(@RequestBody TeacherInfoQueryRequest pageRequest)
    {
        return ResultUtils.success(teacherInfoService.getTeacherInfoOptionVO(pageRequest));
    }

    /**
     * 分页获取教师信息列表（仅管理员可用）
     *
     * @param teacherInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<TeacherInfoVO>> listTeacherInfoByPage(
            @RequestBody TeacherInfoQueryRequest teacherInfoQueryRequest)
    {
        long current = teacherInfoQueryRequest.getCurrent();
        long size = teacherInfoQueryRequest.getPageSize();
        // 查询数据库
        Page<TeacherInfo> teacherInfoPage = teacherInfoService.page(new Page<>(current, size), teacherInfoService.getQueryWrapper(teacherInfoQueryRequest));
        return ResultUtils.success(teacherInfoService.getTeacherInfoVOPage(teacherInfoPage));
    }

    /**
     * 编辑教师信息（给用户使用）
     *
     * @param teacherInfoEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTeacherInfo(@RequestBody TeacherInfoEditRequest teacherInfoEditRequest,
                                                 HttpServletRequest request)
    {
        if (teacherInfoEditRequest == null || teacherInfoEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        TeacherInfo teacherInfo = new TeacherInfo();
        BeanUtils.copyProperties(teacherInfoEditRequest, teacherInfo);
        // 数据校验
        teacherInfoService.validTeacherInfo(teacherInfo, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = teacherInfoEditRequest.getId();
        TeacherInfo oldTeacherInfo = teacherInfoService.getById(id);
        ThrowUtils.throwIf(oldTeacherInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldTeacherInfo, loginUser);
        // 操作数据库
        boolean result = teacherInfoService.updateById(teacherInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/assigned/selection")
    public BaseResponse<List<AssignedTeacherSelectionInfo>> getAssignedTeacherSelectionInfo(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(teacherInfoService.getAssignedTeacherSelectionInfoByTeacherId(loginUser.getId()));
    }

    // endregion

    private void checkIsSelfOrAdmin(TeacherInfo teacherInfo, User loginUser)
    {
        if (!teacherInfo.getCreatorId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

}
