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
import com.caixy.adminSystem.model.dto.classesInfo.*;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.ClassesInfo.AllClassesOptionDataVO;
import com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO;
import com.caixy.adminSystem.service.ClassesInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 班级操作接口
 */
@RestController
@RequestMapping("/classes")
@Slf4j
public class ClassesController
{

    @Resource
    private ClassesInfoService classesInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addClassesInfo(@RequestBody ClassesInfoAddRequest postAddRequest, HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassesInfo post = new ClassesInfo();
        BeanUtils.copyProperties(postAddRequest, post);

        classesInfoService.validClassesInfo(post, true);
        User loginUser = userService.getLoginUser(request);
        post.setCreatorId(loginUser.getId());
        boolean result = classesInfoService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newClassesInfoId = post.getId();
        return ResultUtils.success(newClassesInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteClassesInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ClassesInfo oldClassesInfo = classesInfoService.getById(id);
        ThrowUtils.throwIf(oldClassesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldClassesInfo.getCreatorId().equals(user.getId()) && !userService.isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = classesInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateClassesInfo(@RequestBody ClassesInfoUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassesInfo post = new ClassesInfo();
        BeanUtils.copyProperties(postUpdateRequest, post);

        // 参数校验
        classesInfoService.validClassesInfo(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        ClassesInfo oldClassesInfo = classesInfoService.getById(id);
        ThrowUtils.throwIf(oldClassesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = classesInfoService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ClassesInfoVO> getClassesInfoVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassesInfo post = classesInfoService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(classesInfoService.getClassesInfoVO(post, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ClassesInfo>> listClassesInfoByPage(@RequestBody ClassesInfoQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<ClassesInfo> postPage = classesInfoService.page(new Page<>(current, size),
                classesInfoService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ClassesInfoVO>> listClassesInfoVOByPage(@RequestBody ClassesInfoQueryRequest postQueryRequest,
                                                                     HttpServletRequest request)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ClassesInfo> postPage = classesInfoService.page(new Page<>(current, size),
                classesInfoService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(classesInfoService.getClassesInfoVOPage(postPage, request));
    }

    @PostMapping("get/classes/under-major")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<ClassesInfoVO>> listClassesInfoVOByPageUnderMajor(@RequestBody @Valid ClassesInfoQueryUnderMajorRequest postQueryRequest)
    {
        if (postQueryRequest == null
                || postQueryRequest.getMajorId() == null
                || postQueryRequest.getDepartmentId() == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        return ResultUtils.success(classesInfoService.getClassesInfoVOPageUnderMajor(postQueryRequest));
    }


    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ClassesInfoVO>> listMyClassesInfoVOByPage(@RequestBody ClassesInfoQueryRequest postQueryRequest,
                                                                       HttpServletRequest request)
    {
        if (postQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ClassesInfo> postPage = classesInfoService.page(new Page<>(current, size),
                classesInfoService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(classesInfoService.getClassesInfoVOPage(postPage, request));
    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<ClassesInfoVO>> searchClassesInfoVOByPage(@RequestBody ClassesInfoQueryRequest postQueryRequest,
                                                                       HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ClassesInfo> postPage = classesInfoService.searchFromEs(postQueryRequest);
        return ResultUtils.success(classesInfoService.getClassesInfoVOPage(postPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editClassesInfo(@RequestBody ClassesInfoEditRequest postEditRequest, HttpServletRequest request)
    {
        if (postEditRequest == null || postEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ClassesInfo post = new ClassesInfo();
        BeanUtils.copyProperties(postEditRequest, post);

        // 参数校验
        classesInfoService.validClassesInfo(post, false);
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        ClassesInfo oldClassesInfo = classesInfoService.getById(id);
        ThrowUtils.throwIf(oldClassesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldClassesInfo.getCreatorId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = classesInfoService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 获取学院->专业->班级树形结构
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/4/30 下午2:50
     */
    @PostMapping("/get/classes")
    public BaseResponse<List<AllClassesOptionDataVO>> getClassesOptionDataVOByPage(@RequestBody ClassesInfoQueryRequest postQueryRequest)
    {
        List<DepartmentMajorClassDTO> rawData = classesInfoService.fetchAllClassesData();
        Map<Long, List<DepartmentMajorClassDTO>> groupedByDepartment =
                rawData.stream()
                        .collect(Collectors.groupingBy(dto -> dto.getDepartmentId() != null ? dto.getDepartmentId() :
                                                              -1));

        List<AllClassesOptionDataVO> result = groupedByDepartment.entrySet().stream().map(deptEntry -> {
            DepartmentMajorClassDTO firstDept = deptEntry.getValue().get(0);
            AllClassesOptionDataVO deptOption =
                    new AllClassesOptionDataVO(String.valueOf(deptEntry.getKey()), firstDept.getDepartmentName());
            Map<Long, List<DepartmentMajorClassDTO>> groupedByMajor =
                    deptEntry.getValue().stream()
                            .collect(Collectors.groupingBy(dto -> dto.getMajorId() != null ? dto.getMajorId() : -1));

            List<AllClassesOptionDataVO> majorOptions = groupedByMajor.entrySet().stream().map(majorEntry -> {
                if (majorEntry.getKey() == -1) return null; // Skip if majorId is -1 (null in database)
                DepartmentMajorClassDTO firstMajor = majorEntry.getValue().get(0);
                AllClassesOptionDataVO majorOption =
                        new AllClassesOptionDataVO(String.valueOf(majorEntry.getKey()), firstMajor.getMajorName());
                List<AllClassesOptionDataVO> classOptions = majorEntry.getValue().stream()
                        .filter(classDto -> classDto.getClassId() != null) // Ensure class ID is not null
                        .map(classEntry -> new AllClassesOptionDataVO(String.valueOf(classEntry.getClassId()), classEntry.getClassName(), new ArrayList<>()))
                        .collect(Collectors.toList());
                majorOption.setChildren(classOptions.isEmpty() ? new ArrayList<>() : classOptions);
                return majorOption;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            deptOption.setChildren(majorOptions.isEmpty() ? new ArrayList<>() : majorOptions);
            return deptOption;
        }).collect(Collectors.toList());
        return ResultUtils.success(result);
    }
}
