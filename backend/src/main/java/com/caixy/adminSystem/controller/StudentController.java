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

import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoAddRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoEditRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoQueryRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoUpdateRequest;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.service.StudentInfoService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 学生操作控制器
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController
{

    @Resource
    private StudentInfoService studentInfoService;

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
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addStudentInfo(@RequestBody StudentInfoAddRequest postAddRequest, HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(studentInfoService.addStudentInfo(postAddRequest, request, loginUser));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteStudentInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        boolean b = studentInfoService.deleteStudent(id);
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
    public BaseResponse<Boolean> updateStudentInfo(@RequestBody StudentInfoUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentInfo post = new StudentInfo();
        BeanUtils.copyProperties(postUpdateRequest, post);

        // 参数校验
        studentInfoService.validStudentInfo(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        StudentInfo oldStudentInfo = studentInfoService.getById(id);
        ThrowUtils.throwIf(oldStudentInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = studentInfoService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<StudentInfoVO> getStudentInfoVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentInfo post = studentInfoService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(studentInfoService.getStudentInfoVO(post));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<StudentInfoVO>> listStudentInfoByPage(@RequestBody StudentInfoQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<StudentInfo> postPage = studentInfoService.page(new Page<>(current, size),
                studentInfoService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(studentInfoService.getStudentInfoVOPage(postPage));
//        return ResultUtils.success(postPage);
    }



    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<StudentInfoVO>> listMyStudentInfoVOByPage(@RequestBody StudentInfoQueryRequest postQueryRequest,
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
        Page<StudentInfo> postPage = studentInfoService.page(new Page<>(current, size),
                studentInfoService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(studentInfoService.getStudentInfoVOPage(postPage));
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
    public BaseResponse<Page<StudentInfoVO>> searchStudentInfoVOByPage(@RequestBody StudentInfoQueryRequest postQueryRequest,
                                                         HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<StudentInfo> postPage = studentInfoService.searchFromEs(postQueryRequest);
        return ResultUtils.success(studentInfoService.getStudentInfoVOPage(postPage));
    }

    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editStudentInfo(@RequestBody StudentInfoEditRequest postEditRequest, HttpServletRequest request)
    {
        if (postEditRequest == null || postEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentInfo post = new StudentInfo();
        BeanUtils.copyProperties(postEditRequest, post);

        // 参数校验
        studentInfoService.validStudentInfo(post, false);
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        StudentInfo oldStudentInfo = studentInfoService.getById(id);
        ThrowUtils.throwIf(oldStudentInfo == null, ErrorCode.NOT_FOUND_ERROR);

        boolean result = studentInfoService.updateById(post);
        return ResultUtils.success(result);
    }

}
