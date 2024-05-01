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
import com.caixy.adminSystem.model.dto.subject.SubjectsAddRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectsEditRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectsQueryRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectsUpdateRequest;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.service.SubjectsService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 科目操作控制器
 */
@RestController
@RequestMapping("/subject")
@Slf4j
public class SubjectController
{

    @Resource
    private SubjectsService subjectsService;

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
    public BaseResponse<Long> addSubjects(@RequestBody SubjectsAddRequest postAddRequest, HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Subjects post = new Subjects();
        BeanUtils.copyProperties(postAddRequest, post);
        subjectsService.validSubjects(post, true);
        User loginUser = userService.getLoginUser(request);
        post.setCreatorId(loginUser.getId());
        boolean result = subjectsService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newSubjectsId = post.getId();
        return ResultUtils.success(newSubjectsId);
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
    public BaseResponse<Boolean> deleteSubjects(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Subjects oldSubjects = subjectsService.getById(id);
        ThrowUtils.throwIf(oldSubjects == null, ErrorCode.NOT_FOUND_ERROR);

        boolean b = subjectsService.removeById(id);
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
    public BaseResponse<Boolean> updateSubjects(@RequestBody SubjectsUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Subjects post = new Subjects();
        BeanUtils.copyProperties(postUpdateRequest, post);

        // 参数校验
        subjectsService.validSubjects(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        Subjects oldSubjects = subjectsService.getById(id);
        ThrowUtils.throwIf(oldSubjects == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = subjectsService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SubjectsVO> getSubjectsVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Subjects post = subjectsService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(subjectsService.getSubjectsVO(post, request));
    }

    @GetMapping("/get/all")
    public BaseResponse<List<SubjectsVO>> getAllSubjectsVO()
    {
        return ResultUtils.success(subjectsService.getAllSubjectsVO());
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Subjects>> listSubjectsByPage(@RequestBody SubjectsQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<Subjects> postPage = subjectsService.page(new Page<>(current, size),
                subjectsService.getQueryWrapper(postQueryRequest));
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
    public BaseResponse<Page<SubjectsVO>> listSubjectsVOByPage(@RequestBody SubjectsQueryRequest postQueryRequest,
                                                               HttpServletRequest request)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Subjects> postPage = subjectsService.page(new Page<>(current, size),
                subjectsService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(subjectsService.getSubjectsVOPage(postPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SubjectsVO>> listMySubjectsVOByPage(@RequestBody SubjectsQueryRequest postQueryRequest,
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
        Page<Subjects> postPage = subjectsService.page(new Page<>(current, size),
                subjectsService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(subjectsService.getSubjectsVOPage(postPage, request));
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
    public BaseResponse<Page<SubjectsVO>> searchSubjectsVOByPage(@RequestBody SubjectsQueryRequest postQueryRequest,
                                                                 HttpServletRequest request)
    {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Subjects> postPage = subjectsService.searchFromEs(postQueryRequest);
        return ResultUtils.success(subjectsService.getSubjectsVOPage(postPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSubjects(@RequestBody SubjectsEditRequest postEditRequest, HttpServletRequest request)
    {
        if (postEditRequest == null || postEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Subjects post = new Subjects();
        BeanUtils.copyProperties(postEditRequest, post);

        // 参数校验
        subjectsService.validSubjects(post, false);
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        Subjects oldSubjects = subjectsService.getById(id);
        ThrowUtils.throwIf(oldSubjects == null, ErrorCode.NOT_FOUND_ERROR);

        boolean result = subjectsService.updateById(post);
        return ResultUtils.success(result);
    }

}
